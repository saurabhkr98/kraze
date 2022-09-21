The repository is written in java with spring boot as framework and jUnit for writing test cases. An in memory caching is implemented using a static hashMap.

When you run the DemoApplication an endpoint is exposed in your local machine with the below curl.
curl --location --request GET 'http://localhost:8080/get/location/file?apiKey=your_api_key&filePath=/file/path/input.txt'

You can import this curl in postman and hit the api with your file path (containing city name) and google apiKey. 
There are several cases which are handled like file not found exception, empty file exception, wrong api key exception, google client down exception, file write exception.
But if no exception is thrown then an output.txt file will be generated in the folder with current timestamp for example folder name can be 2022-09-21T11:09:23.849239.

You can also run the junit test cases in class LocationServiceImplTest

You can compile the program using `mvn clean install`

You can skip the test cases and compile using `mvn clean install -DskipTests`

Below is an example of what output.txt file will look like if it is generated and status code will be 200
12.34,16.78
15.34,10.78
8.34,24.78

For the failed requests you will receive errormessage and error status code (4xx or 5xx)


