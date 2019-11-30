package com.codebrew.encober.network.socket

import com.codebrew.encober.utils.AppConstants
import com.codebrew.encober.utils.local.UserManager
import io.socket.client.Ack
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import timber.log.Timber

class SocketManager(private val userManager: UserManager) {
    companion object {


        private const val SOCKET_URL = "http://52.35.234.66:8001"
//        private const val SOCKET_URL = "http://52.35.234.66:8000"



        private const val EVENT_CONNECT = "socketConnected"
        const val EVENT_RECEIVE_MESSAGE = "message"
        const val EVENT_SEND_MESSAGE = "sendMessage"

        private var INSTANCE: SocketManager? = null

        fun getInstance() = INSTANCE ?: synchronized(SocketManager::class.java) {
            INSTANCE ?: SocketManager(UserManager)
                .also {
                    INSTANCE = it
                }
        }

        /**
         * Disconnects from current instance and also releases references to it
         * so that a new instance will be created next time.
         * */
        fun destroy() {
            Timber.d("Destroying socket instance")
            INSTANCE?.disconnect()
            INSTANCE = null
        }
    }

    private val options by lazy {
        IO.Options().apply {
            reconnection = true
            forceNew = true
            query = "id=${userManager.getUserProfile()?.id}&userType=${AppConstants.CHAT_TYPE_USER}"
        }
    }

    private val socket by lazy { IO.socket(SOCKET_URL, options) }

    fun connect() {
        if (socket.connected()) {
            Timber.d("Socket is already connected")
            return
        }

        if (!socket.hasListeners(EVENT_CONNECT)) {
            socket.on(EVENT_CONNECT) { Timber.d("Socket Connect ${socket.id()}") }
            socket.on(Socket.EVENT_DISCONNECT) { Timber.d("Socket Disconnect") }
            socket.on(Socket.EVENT_CONNECT_TIMEOUT) { args -> Timber.w("Socket Connect timeout : ${args.firstOrNull()}") }
            socket.on(Socket.EVENT_ERROR) { args -> Timber.w("Socket error : ${args.firstOrNull()}") }
            socket.on(Socket.EVENT_CONNECT_ERROR) { args -> Timber.w("Socket connect error : ${args.firstOrNull()}") }
        }

        socket.connect()
    }

    fun disconnect() {
        socket.disconnect()
        socket.off()
        Timber.d("Disconnect")
    }

    fun on(event: String, listener: Emitter.Listener) {
        socket.on(event, listener)
    }

    fun off(event: String, listener: Emitter.Listener) {
        socket.off(event, listener)
    }

    fun emit(event: String, args: Any, acknowledge: Ack) {
        if (socket.connected()) {
            socket.emit(event, args, acknowledge)
        }
    }
}