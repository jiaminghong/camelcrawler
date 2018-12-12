# Camel Crawler
//TODO-description


#### Web Crawler

- [x] Write basic crawler to get list of URLs for single domain
- [X] Feed base root URLs from text file
- [X] Connect crawler to a persistence database [MySQL]
- [X] Convert the list of domains into a data frame/structure for analysis
- [X] Build a database of domains to crawl
- [ ] Blacklist websites using CDNs/dynamic websites 

#### Middleware develops API for the database - Redis

- [ ] Configure MySQL to Redis
- [ ] Redis - graph data structure to retrieve required information
- [ ] Write end-points to retrieve data-points for Front-end

#### Front-end REACT

- [ ] Display the total number of backlinks in the database
- [ ] Query the API to retrieve total number of backlinks

#### TestCases
- [X] Domain Object Spec
- [ ] JDBC Connection Spec
- [ ] Crawler Spec
- [ ] Webclient Spec
- [ ] getBody() Spec -> parser

### How to Run It

//TODO

### Advance Features

- [ ] Work with dynamic website by implementing a headless browser - HTMLUnit OR Selenium
- [ ] Rotating Proxies to avoid bot check & circumnavigate CDNS e.g. cloudFare, Distill Network
- [X] Building **jar** file of the web crawler to work on multiple physical machines -> Akka Compile Issue
- [ ] Memory optimization - use of CountdownLatcher
- [ ] Integrate Kafka+Kafka API to stream data from crawler to database and onto a front-end for 'real-time' feedback


## Team Members 
- Abdusamed
- Ming
- Chi
