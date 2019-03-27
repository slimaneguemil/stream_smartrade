## About this Project
The aim of this project is to established an agnostic reactive client and communicate with any message-broker.
It uses the reacting programming paradigm from end to end of the flow.

It is composed of 4 Modules:

BrokerConnector Module: 
It contains all the underliying logic to communicate with broker  ( based on SpringCLoud Stream)
It creates automatically 2 channels : output channel for outgoing messages, input channel for incoming.
It generate  2 bean services for each reactive framework : rxjava and reactor .

ClientRxJava Module:
It contains a Rxjava  end client to establish reacting communication with BrokerConnector.
with the properties we can design dynamically channel communication with brokers.
see unit testing to see functionnalities.

ClientReactor Module:
It contains a Reactor end client to establish reacting communication with BrokerConnector.
with the properties we can design dynamically channel communication with brokers.
 
ClientRest :
point-to-point solution based on spring Webflux.


in study:
ClientWebsocket /ServerWebsocket: web socket client-server solution to establish connection with Brokerconnectorbased on Reactor.
clientRSocket/ServerRSocket : rsocket client-server solution to establish connection with Brokerconnectorbased on Reactor.
