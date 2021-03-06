# acl-policies-devtest
This is an old and no longer used development test.

You can read what was requested in this file: [DEVTEST.md](DEVTEST.md)

### Overview
The application is built as a war that can run on an embedded tomcat. It's done using Spring Boot and I've only added commons-lang as a dependency to increase the amount of code I write to make it easier to evaluate the test although in a real case I would have used external libraries especially to avoid errors in the range calculation of the CIDR.

I've chosen to follow the concepts of Domain Driven Design to create a very object-oriented layer where the objects mix data and behavior and separate it well from other layers such as the one in charge of the infrastructure to expose the REST API.

The design patterns that I thought convenient to use are:
- Builder
- Facade
- Data Transfer Objects (DTO)

I didn't want to force the readability of the code for using more design patterns.

### Usage
To start the embedded server, run:
```
mvnw tomcat7:run
```
Server will run on port `8080`

### Exposed REST endpoints
- **GET `/aclpolicies/acl`**
  - **Description:** Returns the whole ACL 
  - Example response:
  
```
        [{
            "id": 1,
            "source": "192.168.0.10",
            "destination": "192.168.0.2",
            "protocol": "tcp/80",
            "action": "allow"
        },{
            "id": 2,
            "source": "88.1.12.225",
            "destination": "99.235.1.15",
            "protocol": "tcp/80,8080",
            "action": "deny"
        }]
```
- **GET `/aclpolicies/acl/:id`**
  - **Description:** Returns the single rule given the id
  - Example response:
```
        {
            "id": 1,
            "source": "192.168.0.10",
            "destination": "192.168.0.2",
            "protocol": "tcp/80",
            "action": "allow"
        }
```
- **POST `/aclpolicies/acl`**
  - **Description:** Return the first rule that their fields match with the packet
fields in order to know what action to apply. For example, for a packet
with fields: [source=”192.168.0.5”, destination=”192.168.0.1” and
protocol=”UDP/80”] the first rule that match it will be the number 3.
Note that the IP address 192.168.0.5 is contained in the subnet
192.168.0.0/24, the IP 192.168.0.2 is contained in the subnet
192.168.0.0/28 and udp/80 is a subset of the udp/any.
  - Example request body:
```
        {
            "source": "192.168.0.5",
            "destination": "192.168.0.1",
            "protocol": "udp/80"
        }
```
  - Example response:
```
        {
            "id": 3,
            "source": "192.168.0.0/24",
            "destination": "192.168.0.0/28",
            "protocol": "udp/any",
            "action": "allow"
        }
```
