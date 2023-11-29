# Status Tracker

This is just a project I've been working on to keep my mind busy, and gave me a chance to play with ktor.

The idea is simple, in my work I've come across many points in which I have some process, say configuring a new customer,
that has multiple steps, often with third party reliances, with a required order as data would flow between each step. 
The front ends would generally trigger the request through a REST or GraphQL endpoint, and attempt to hold the connection 
until the process had finished. However, the restrictions we would have on these servers would generally only allow for 
it to be held for 1 minute at most.

Instead of holding that connection, status tracker provides an endpoint built to be polled against as quickly as you'd like,
getting super-fast response times (locally averaging ~5ms), as it's essentially a facade to a redis command. Your backend
service can make a "trackable" with status tracker, and post updates to it as it completes steps. Status tracker will ensure
your steps happen in the order you expect, and will (once implemented) support an "Error Track" where you can define a set
of statuses, as often these multistep processes will have multiple steps in handling a failure, especially when there are
third party services involved.

[OpenAPI Documentation (Generated from ktor endpoints!)](./src/main/resources/openapi/documentation.yaml)

[Postman collection I've been using to play with it](./status%20tracker.postman_collection.json)