package org.antbiz.antbiz_framework.backend.lib

import java.time.ZoneId
import java.util.Date

fun Date.toLocalDateTime() = this.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()