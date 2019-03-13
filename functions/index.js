const functions = require('firebase-functions');
const admin = require('firebase-admin');

admin.initializeApp();

var db = admin.database();
var pendingMessages = db.ref('/pendingMessages');
var processedMessages = db.ref('/processedMessages');

exports.sendMessage = functions.database.ref('/pendingMessages/{msgID}')
.onCreate((snapshot, context) => {
        console.log(snapshot);
        const senderID = snapshot.val().senderID;
        const receiveID = snapshot.val().receiveID;
        const token = snapshot.val().receiveToken
        const msgID = context.params.msgID;
        console.log(msgID)
        const senderName = snapshot.val().senderName;
        return processedMessages.ref.child(msgID).child('receiveStatus').set('pending').then((snapshot) => {
            var notify = {
                notification: {
                    title: 'You got Poked by ' + senderName,
                    body: 'Success!'
                },
                data: {
                    senderName: senderName,
                    senderID: senderID,
                    msgID: msgID
                },
                token: token
            };
            admin.messaging().send(notify).then((response) => {
                return processedMessages.ref.child(msgID).child('sendStatus').set('success');
            })
            .catch((error) => {
                console.log('Error sending message:', error);
            });
        });

});