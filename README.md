# Flame - Spark test application


## Building using build packs

```
pack build com.mastercard.spark.flame:latest --tag com.mastercard.spark.flame:1.0.0  --builder gcr.io/buildpacks/builder:google-22
```

Load into kind cluster called 'spark'
```
kind load -n spark docker-image com.mastercard.spark.flame:1.0.0 
```

## HTTP Status Codes

GET operation returning any HTTP status code. Eg 201
```
% ./src/test/curl/get-status 201                 
GET http://localhost:8080/status/201
status: 201
time: 0.193209
response:
Status code 201
```

or 404

```
% ./src/test/curl/get-status 404
GET http://localhost:8080/status/404
status: 404
time: 0.012496
response:
Status code 404
```

## Echo

Echo a message
```
% ./src/test/curl/get-echo Hello
GET http://localhost:8080/echo?msg=Hello
status: 200
time: 0.224101
response:
Hello
```

## Workloads

Create a workload using memory, CPU and time resources.

For example create a workload that waits 500ms, uses 2,000,000 bytes of memory
with a CPU load of 1,000,000 (number of SHA256 hash operations) using 4 threads:
```
% ./src/test/curl/post-workload NORMAL 500 1000000 2000000 4 
POST http://localhost:8080/workloads
request:
{
  "description": "test workload",
  "action": "NORMAL",
  "duration": 500,
  "cpuLoad": 1000000,
  "memoryLoad": 2000000,
  "numThreads": 4
}

status: 200
time: 1.874151
response:
{
  "id": "71fae536-c5ea-4815-a918-17bb8e66abb3",
  "timestamp": "2024-11-04T08:58:57.130063Z",
  "replica": "29385@C02ZK7AAMD6P-MCLABS",
  "elapsedTime": 1766,
}
```

## Build Status

```
% ./src/test/curl/get-build-status
GET http://localhost:8080/build/status
status: 200
time: 0.015810
response:
{
  "version": "1.0.0-SNAPSHOT",
  "commitId": "8fc84954888903aa150f415c37b596c40d5161bf"
}
```

## Actuator Endpoints

```
% ./src/test/curl/curl-request GET actuator/health
GET http://localhost:8080/actuator/health
status: 200
time: 0.034439
response:
{
  "status": "UP"
}
```


