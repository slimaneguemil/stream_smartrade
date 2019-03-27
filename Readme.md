## About this Project
The aim of this project is to established a agnostic client able to communicate with any message-broker.
It uses the reacting programming paradigm from end to end of the flow.

It is composed of 4 Modules:

BrokerConnector Module: 
It contains all the underliying logic to communicate with brokers: with SpringCLoud Stream.
It creates 2 channels : output for outgoing , input for incoming.
It creates 2 services for each reactive framework : rxjava and reactor .

ClientRxJava Module:
It contains a Rxjava  end client to establish reacting communication with BrokerConnector.
with the properties we can desin dynamically channel communication with brokers.
see unit testing to see functionnalities.

ClientReactor Module:
It contains a Reactor end client to establish reacting communication with BrokerConnector.
with the properties we can desin dynamically channel communication with brokers.

ClientRest :
in progress to communicate with spring Webflux.


