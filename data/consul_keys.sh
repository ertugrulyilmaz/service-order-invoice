#!/usr/bin/env bash

curl -X PUT localhost:8500/v1/kv/app/datasource/dbConnectionUrl           -d 'jdbc:postgresql://localhost:5432/order_management_service?useSSL=false&autoReconnect=true&useUnicode=true&characterEncoding=UTF-8&characterSetResults=utf8&connectionCollation=utf8_general_ci'
curl -X PUT localhost:8500/v1/kv/app/datasource/dbDriverClassName         -d 'org.postgresql.Driver'
curl -X PUT localhost:8500/v1/kv/app/datasource/dbUsername                -d 'eriks'
curl -X PUT localhost:8500/v1/kv/app/datasource/dbPassword                -d 'eriks'
curl -X PUT localhost:8500/v1/kv/app/datasource/hikariIdleTimeout 		    -d '10'
curl -X PUT localhost:8500/v1/kv/app/datasource/hikariMinimumIdle 		    -d '1'
curl -X PUT localhost:8500/v1/kv/app/datasource/hikariMaximumPoolSize 	  -d '5'
curl -X PUT localhost:8500/v1/kv/app/datasource/hikariConnectionTimeout   -d '2000'
curl -X PUT localhost:8500/v1/kv/app/datasource/hikariMaxLifetime 		    -d '0'

curl -X PUT localhost:8500/v1/kv/app/security/jwtAuthExpMinute 		        -d '10'
curl -X PUT localhost:8500/v1/kv/app/security/jwtAuthSecret 	            -d 'ABADE673C5C433253617F5B6482538D8BF226A13FC6CA536EB71541DCF75279EABADE673C5C433253617F5B6482538D8BF226A13FC6CA536EB71541DCF75279EABADE673C5C433253617F5B6482538D8BF226A13FC6CA536EB71541DCF75279E'

curl -X PUT localhost:8500/v1/kv/app/executorConfig/corePoolSize          -d '10'
curl -X PUT localhost:8500/v1/kv/app/executorConfig/maxPoolSize           -d '50'
curl -X PUT localhost:8500/v1/kv/app/executorConfig/queueCapacity         -d '10000'

curl -X PUT localhost:8500/v1/kv/app/aws/region                           -d 'eu-west-1'
curl -X PUT localhost:8500/v1/kv/app/aws/kinesis/streamName               -d 'service-order'
