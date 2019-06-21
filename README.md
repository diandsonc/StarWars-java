[![Build Status](https://travis-ci.com/diandsonc/StarWars.svg?branch=master)](https://travis-ci.com/diandsonc/StarWars)
[![codecov](https://codecov.io/gh/diandsonc/StarWars/branch/master/graph/badge.svg)](https://codecov.io/gh/diandsonc/StarWars)

# StarWars API   
API simples de cadastro de planetas com integração com https://swapi.co/. 




# API Endpoints
|Endpoint                               | Functionality                      |HTTP method 
|---------------------------------------|------------------------------------|-------------
|/v1/planets                            |Add a planet                        |POST        
|/v1/planets/*planet_id*                |modify a planet’s information       |PUT
|/v1/planets/*planet_id*                |Remove a planet                     |DELETE
|/v1/planets                            |Retrieves all planets               |GET
|/v1/planets/search/id/*planet_id*      |Search a planet by id               |GET
|/v1/planets/search/name/*planet_name*  |Search a planet by name             |GET
