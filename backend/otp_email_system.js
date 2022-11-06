var express = require('express');
var app = express();
var PORT = 8080;

// For parsing application/json
app.use(express.json());

// For parsing application/x-www-form-urlencoded
app.use(express.urlencoded({ extended: true }));

app.listen(PORT, function (err) {
    if (err) console.log(err);
    console.log("Server listening on PORT", PORT);
});

var start = 0;  // to recored time at which RequestOtpButton Hits
var end = 0;    // to record time at which Opt is being entered for verification
var dur = 0;    // to check time interval of 30 seconds

class User {
    constructor(email, otp) {
        this.email = email;
        this.otp = otp;
    }
}
const currentUser = new User("Abc", 12345);    // Sample data 


function generateOTP()      // Generating a 5 digit Random Number 
{
    start = new Date().getTime();
    currentUser.otp = Math.floor(Math.random() * 100000 + 1)
    console.log(currentUser.otp);
    return true;
}

// RequestOtpButton - After Entering Email ID
app.post('/login', function (req, res) {
    try {

        start = new Date().getTime();
        console.log(" Request Body Contains - email : " + req.body.email);
        if (generateOTP() == true) {
            currentUser.email = req.body.email;
            res.status(200).json({
                message: 'Otp Successfully Generated for ' + currentUser.email
            });


            console.log(currentUser);
            //  console.log(start+" "+end+" "+dur);
        }
        else
            res.status(503).json({ message: "Service Unavailable " })
    }
    catch (err) {
        console.log(err.message);
    }
});

// Verify OTP Button - After Otp is being generated
app.post('/verify', function (req, res) {
    try {
        end = new Date().getTime();
        dur = end - start;
        console.log(" Request Body Contains - OTP Entered By User : " + req.body.entered_Otp);
        if (req.body.entered_Otp == currentUser.otp && dur < 30000) {
            console.log("OTP validation Successful for User " + currentUser.email);
            res.status(200).json({
                message: 'OTP validation Successful for User ' + currentUser.email
            });
            // Show User their Email ID as per USE-CASE
        }
        else {
            if (req.body.entered_Otp != currentUser.otp) {
                console.log("OTP is InCorrect");
                res.status(409).json({ message: "OTP is InValid , please generate again " })
            }
            else {
                console.log("Time Limit Exceeded 30 seconds");
                res.status(410).json({ message: "OTP is Expired , please generate again " })
            }
        }
    }
    catch (err) {
        console.log(err.message);
    }
});

// If Incorrect OTP/ Time Limit exceeds 30 Seconds , then request for new otp
app.post('/requestAnotherOtp', function (req, res) {
    try {

        start = new Date().getTime();
        console.log(" Request Body Contains - email : " + req.body.email);
        if (generateOTP() == true) {
            currentUser.email = req.body.email;
            res.status(200).json({
                message: 'Otp Successfully Generated for ' + currentUser.email
            });
            console.log(currentUser);
            // console.log(start + " " + end + " " + dur);
        }
        else
            res.status(503).json({ message: "Service Unavailable " })
    }
    catch (err) {
        console.log(err.message);
    }
});

// To LogOut of the System  - Button
app.get('/logout', (req,res) => {
    try {
        currentUser.email="mnoi09776";
        currentUser.otp=generateOTP();  // Any unknown email & OTP , so nobody can access it .
        res.status(200).json({
            message: 'Logged Out Successfully'
        });
    } catch (err) {
        res.status(500).send(err);
    }
})