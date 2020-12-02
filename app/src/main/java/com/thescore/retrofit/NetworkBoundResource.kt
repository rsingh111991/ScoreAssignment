package com.thescore.retrofit


import com.fernandocejas.arrow.optional.Optional.absent
import com.fernandocejas.arrow.optional.Optional.fromNullable
import com.thescore.retrofit.connection.IInternetObserver
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableEmitter
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import retrofit2.Response
import timber.log.Timber
import java.io.IOException
import java.util.concurrent.atomic.AtomicInteger

abstract class NetworkBoundResource<RemoteType, LocalType>(
    internetObserver: IInternetObserver,
    emitter: FlowableEmitter<Resource<LocalType>>
) {
    init {
        execute(internetObserver, emitter)
    }

    private fun execute(
        internetObserver: IInternetObserver,
        emitter: FlowableEmitter<Resource<LocalType>>
    ) {
        val compositeDisposable = CompositeDisposable()
        val throwableMain: BehaviorSubject<com.fernandocejas.arrow.optional.Optional<Throwable>> =
            BehaviorSubject.createDefault(
                absent()
            )

        val fetchRemote = shouldFetchFromRemote()
        val resourceStatus: BehaviorSubject<Status> =
            BehaviorSubject.createDefault(if (fetchRemote) Status.LOADING else Status.SUCCESS)
        compositeDisposable.add(resourceStatus.toFlowable(BackpressureStrategy.LATEST)
            .switchMap {
                Flowable.combineLatest(getLocal().switchIfEmpty(Flowable.never()),
                    throwableMain.toFlowable(BackpressureStrategy.LATEST),
                    { t1: LocalType, t2 -> Pair(t1, t2) })
            }
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.newThread())
            .subscribe({ result ->
                emitter.onNext(
                    Resource.newResource(
                        resourceStatus.value!!, result.first,
                        result.second.orNull()
                    )
                )
            },
                {
                    Timber.e(it)
                    emitter.onNext(Resource.newResource(Status.ERROR, null, it))
                })
        )

        if (fetchRemote) {
            compositeDisposable.add(getFromRemote()
                .retryWhen { networkThrowable ->
                    val counter = AtomicInteger()
                    networkThrowable
                        .takeWhile { counter.getAndIncrement() != 3 }
                        .doOnNext { throwable ->
                            if (throwable is IOException) {
                                if (counter.get() == 1) {
                                    resourceStatus.onNext(Status.ERROR)
                                    throwableMain.onNext(fromNullable(throwable))
                                }
                            } else {
                                throw Exception(throwable)
                            }
                        }.flatMap {
                            internetObserver.observerInternetConnection()
                                .toFlowable(BackpressureStrategy.LATEST)
                        }.doOnNext { connected -> Timber.d("Connected status %s", connected) }
                        .filter { connected -> connected }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe({ netWorkResponse ->
                    if (netWorkResponse.isSuccessful) {
                        resourceStatus.onNext(Status.SUCCESS)
                        saveCallResult(mapper().apply(netWorkResponse.body()!!))
                    } else {
                        resourceStatus.onNext(Status.ERROR)
                        throwableMain.onNext(fromNullable(RuntimeException()))
                    }
                }, {
                    resourceStatus.onNext(Status.ERROR)
                    throwableMain.onNext(fromNullable(it))
                }
                ))
        }
        emitter.setCancellable {
            compositeDisposable.dispose()
        }

    }

    /**
     * get database from room if available
     */
    abstract fun getLocal(): Flowable<LocalType?>
    abstract fun getFromRemote(): Single<Response<RemoteType>>

    /**
     * map data to store in database
     */
    abstract fun mapper(): Function<RemoteType, LocalType>

    /**
     * Save data to room database
     */
    abstract fun saveCallResult(dataResponse: LocalType)
    abstract fun shouldFetchFromRemote(): Boolean
}