#!/bin/bash

./gradlew clean build -x test && java -ea -Dspring.profiles.active=dev -jar build/libs/battleship-game-server-1.00.jar
