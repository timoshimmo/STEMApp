package app.com.android.ihsteachers.models

class ChatModel {

    var sender: String? = null
    var message: String? = null
    var timeSent: String? = null
    var recipient: String? = null

    constructor(){}

    constructor(sender: String?, message: String?, timeSent: String?, recipient: String?) {
        this.sender = sender
        this.message = message
        this.timeSent = timeSent
        this.recipient = recipient
    }

}