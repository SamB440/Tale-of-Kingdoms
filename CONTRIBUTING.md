Contributing to Tale of Kingdoms
================================
We're very lenient with merge requests and would like to invite everyone to contribute where possible!

**Table of contents:**
* [Requirements](#requirements)
* [Merge Request Policy](#merge-request-policy)
* [Frequently Asked Questions](#frequently-asked-questions)
    * [I get an error in another environment](#i-get-a-noclassmethod-found-error-when-running-on-a-different-environment)
    * [How do I make my code support a dedicated server environment?](#how-do-i-make-my-code-support-a-dedicated-server-environment)

## Requirements

To start contributing to Tale of Kingdoms, you will need the following:
- JDK 16, you can find this at [AdoptOpenJDK](https://adoptopenjdk.net).
- Git / git bash
- Gradle
- A mouse and keyboard

## Merge Request Policy

Pretty much anything that would improve the mod will be accepted, just as long as you follow good conventions!

If you are adding server-side code 
## Frequently Asked Questions

### I get a NoClass/Method found error when running on a different environment
This is due to some methods and classes not being available on the client, or the dedicated Minecraft server.
Check if the method is available for your environment by using `Ctrl + Click` on the method or checking the super class,
and you will be able to see the `@Environment` annotations denoting it.

### How do I make my code support a dedicated server environment?
Make sure it is run on the correct thread and that correct methods are used for server/client compatibility.
The `TaleOfKingdomsAPI` class has a lot of useful methods for scheduling and running on the different threads.

You may need to add a packet (especially if you are trying to add support via a `Screen`), but we have very useful
classes for that on both server and client. See `ClientPacketHandler` and `ServerPacketHandler`. You'll also need to
make sure the packet cannot be abused on the dedicated server (such as giving infinite coins) and add appropriate limits.
