package com.thescore.teams.uimodel

import com.thescore.persistence.entity.Teams

data class ActionableItem(val team: Teams, val clickAction: () -> Unit)
