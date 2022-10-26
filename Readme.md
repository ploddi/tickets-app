# Tickets4Sale MVP

As a first engineer allowed in John basement I was very excited with a task.
I and John both agreed that implementation speed is the most important thing 
and decided to go with simple MVP implementation first as proof of concept.
John would like to see how possible web app and UX can look like, but to make
frontend app possible I should implement backend API MVP first.

I decided to go "full ZIO" on this greenfield project and use `zio-http`, which has
not been properly released yet. So in order to use `zio-http` I had to download `zio-http` sources 
to local machine, build and publish all artefacts locally (as described https://github.com/zio/zio-http/issues/1473).

API draft was already been prepared by John, so I implemented some case classes for API request/response
serialization layer (can be found in `models.api` package). JSON was chosen as payload serialization format, 
so I decided to go with `zio-json` as JSON library of choice. `zio-json` supports auto decoder/encoder generation, 
so it was easy and with minimal boilerplate.

In order to provide MVP as-soon-as-possible I used in-memory storage instead of proper persistence.
`services.Inventory` provides ZIO API to storage. Actual pure state management is implemented in
`models.storage` package. In-memory state is protected from concurrent access by `ZIO.Ref`.
Pretty much all business related constants are accumulated in `models.Performance` object,
and can be potentially factored out as config values in the future.

Initial state for MVP is loaded from CSV resource bundled with app. I decided to go with
`kantan.csv` library for CSV parsing and loading. CSV loading code can be found in 
`services.InventoryLoader`.

HTTP is handled by `endpoint.TicketsHttp`, which is responsible for routing and req/res parsing and
delegating method calls to `services.Inventory` service.

Due to fact, that MVP uses in-memory storage, which probably will be swapped with actual
persistent storage layer, I decided not to cover `services.InMemoryInventory` with unit tests,
but rather cover the whole app with suite of integration tests (which can be used as reference
for testing with future persistence layer).

App can be executed via `sbt run` command (but you should prepare local `zio-http` snapshots first!)
or via Docker command `docker run -p 8080:8080 ploddi/tickets4sale:latest`