package org.piotrwyrw.redkit

import java.util.logging.Level

fun log(msg: String) = RedKit.getInstance().logger.log(Level.INFO, msg)
fun error(msg: String) = RedKit.getInstance().logger.log(Level.SEVERE, msg)