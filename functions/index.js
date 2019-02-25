const functions = require('firebase-functions');
const admin = require('firebase-admin');

admin.initializeApp();

var db = admin.database();
var pendingMessages = db.ref('/pendingMessages');
var processedMessages = db.ref('/processedMessages');

exports.sendMessage = functions.database.ref('/pendingMessages')
.onWrite((change, context) => {
    if (change.before.exists()) {
        return null;
    }
    if (!change.after.exists()) {
        return null;
    }
    pendingMessages.on("child_added", function(snapshot) {
        const senderID = snapshot.val().senderID;
        const receiveID = snapshot.val().receiveID;
        const token = snapshot.val().receiveToken;
        const msgID = snapshot.key;
        return processedMessages.ref.child(msgID).child('receiveStatus').set('pending').then((snapshot) => {
            var userRef = db.ref('users/' + senderID);
            userRef.on("value", function(snapshot) {
            const name = snapshot.val().name;
            var notify = {
                notification: {
                    title: 'You got Poked by ' + name,
                    body: 'Success!'
                },
                data: {
                    senderName: name,
                    senderID: senderID,
                    msgID: msgID
                },
                token: "eYa1BSUeT6k:APA91bHF4PyfSJpswk3pxlFcOBSMT-ueN9NFHTWrBg0TUAbrlxq5k-waA_DDCvuiHr-G7Xo-h1FgxFkkHtwmKEObUpPjGk2bNvS1o2JjAlcEB_0O4HR_MeXzE2jtd1qThzWoOF1v8g1R"
            };
            admin.messaging().send(notify).then((response) => {
                processedMessages.ref.child(msgID).child('sendStatus').set('success');
            })
            .catch((error) => {
                console.log('Error sending message:', error);
            });
            }, function (errorObject) {
                console.log("The read failed: " + errorObject.code);
            });
        });
    });
});