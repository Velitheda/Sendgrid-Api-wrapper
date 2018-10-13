# SendGrid Email Service

This is a simple service that provides a wrapper around the endpoint SendGrid
exposes to send mail.

It's a Spring Boot application written with maven.

It currently has one endpoint, `/v1/send` which accepts a POST
request with the following json format that is to be used in the body:

```
    {
        "to": [
            "test@example.com"
        ],
        "subject": "your subject",
        "body": "your email body",
        "cc": [
            "test@example.com"
         ],
         "bcc": [
            "test@example.com"
         ]
    }
```

The 'cc' and/or the 'bcc' fields can be left off entirely. The other three are required.

You must also specify at least one address in the 'to' array.

Calling this endpoint will prompt SendGrid to send an email with the account you configure
the application with to send an email with these fields populated.

### Authentication
In order to connect to SendGrid, you load your api key into an environment variable. The app essentially closes over it and allows
what it assumes are trusted sources to call it. You could also create a separate .properties file to read it from and
add that one to your gitignore so you don't accidentally commit it if you like.

This prevents more apps than necessary having access to your api key and potentially risking it getting exposed
as apps send it to this one across network calls and store it in their config as well.

Then it will need to be integrated with the authentication system your other microservices use before it is deployed publicly.

WARNING: IF YOU DEPLOY THIS APP IN ITS CURRENT STATE ON A SERVER THAT EXPOSES ITSELF PUBLICLY TO THE WEB,
ANYONE WILL BE ABLE TO SEND EMAILS WITH YOUR SENDGRID ACCOUNT.

### Ways to continue this project

The following describes how this application could be continued to be developed if it was in a real organisation with a
microservices architecture.

The first thing I would do would be to spend more time understanding the use cases of which applications/users will be using this,
and get them to try it out and see if this fits their needs or not. Then work our what additional features they need if it does not.

Once it's been defined how other applications are going to use the application, it might also be useful to define contract tests
between the systems as well to provide a higher guarantee of preventing accidental changes between systems.

If the service is intended to be public facing and allows an external client to call it directly, eg a 'contact us' form on a web page
or a 'help' page on an app with no authentication involved to enable it to be easier to send queries to the customer service team, then
as long as emails that aren't sent to the organisation's domain are filtered out, it would only likely be protection from ddos attacks
that would still need to be considered.

Otherwise, the service should be integrated with the system of authentication that the rest of the microservices use.
For example, it may need to be changed to also accept an access token that it can use to verify the caller is allowed to send emails
with the service.

Ideally it would also be separately security reviewed by either a qualified in-house security professional or sent to an
external company to check carefully for risks of possibilities of the api key or the mail sending ability to be hijacked,
as these could have quite drastic consequences for the organisation.

To deploy it, I would probably write something to package the app as a Docker container image.
I'd then set up a CI pipeline on GitLab that would on a commit with a release tag, run all the unit tests,
then build the docker image, and then deploy the application onto AWS Elastic Beanstalk as it is simple and easy to set up
as a lot of things are managed for you, (eg. load balancing, autoscaling, etc.)

This could be potentially considered for deploying as an AWA Lambda application, as it is stateless, quick to execute, lightweight, and quite fast to start up.

Once it has been successfully deployed to a staging environment, unless it is expected to handle very minimal volumes,
automated load tests should also be written and added to the CI pipeline to confirm it can perform under stress.