// PRACTICAL 07: USER PROFILES & ACTIVITY LOGGING
// Install: npm install express mongoose
// Run: node Practical_07_User_Profiles_Activity_Logging.js

const express = require("express");
const mongoose = require("mongoose");

const app = express();
app.use(express.json());

const MONGO_URI =
    process.env.MONGO_URI || "mongodb://127.0.0.1:27017/bookflow_users";

const userSchema = new mongoose.Schema({
    username: {
        type: String,
        required: true
    },
    email: {
        type: String,
        required: true,
        unique: true
    },
    profile: {
        type: mongoose.Schema.Types.Mixed,
        default: {}
    }
});

const activitySchema = new mongoose.Schema({
    user_id: {
        type: mongoose.Schema.Types.ObjectId,
        required: true
    },
    action: {
        type: String,
        required: true
    },
    timestamp: {
        type: Date,
        default: Date.now
    }
});

const User = mongoose.model("User", userSchema);
const Activity = mongoose.model("Activity", activitySchema);

// POST /users
app.post("/users", async (req, res) => {
    try {
        const user = await User.create(req.body);
        res.status(201).json(user);
    } catch (error) {
        res.status(400).json({ error: error.message });
    }
});

// GET /users/:id
app.get("/users/:id", async (req, res) => {
    try {
        const user = await User.findById(req.params.id);

        if (!user) {
            return res.status(404).json({ error: "User not found" });
        }

        res.json(user);
    } catch (error) {
        res.status(400).json({ error: "Invalid user ID" });
    }
});

// POST /activities
app.post("/activities", async (req, res) => {
    try {
        const activity = await Activity.create(req.body);
        res.status(201).json(activity);
    } catch (error) {
        res.status(400).json({ error: error.message });
    }
});

// GET /activities/:user_id
app.get("/activities/:user_id", async (req, res) => {
    try {
        const activities = await Activity
            .find({ user_id: req.params.user_id })
            .sort({ timestamp: -1 });

        res.json(activities);
    } catch (error) {
        res.status(400).json({ error: "Invalid user ID" });
    }
});

async function startServer() {
    try {
        await mongoose.connect(MONGO_URI);
        console.log("MongoDB connected");

        app.listen(5001, () => {
            console.log("User service running on port 5001");
        });
    } catch (error) {
        console.error("MongoDB connection failed:", error.message);
        process.exit(1);
    }
}

startServer();
