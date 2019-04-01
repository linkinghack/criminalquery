.PHONY:
# export GO111MODULE=on
GIT_TAG=`git describe --always --tags`
BUILD_TIME=`date +%FT%T%z`
LDFLAGS=-ldflags "-w -s -X version.GitTag=${GIT_TAG} -X version.BuildTime=${BUILD_TIME}"
# DOCKER_RUN=docker run --rm -v ${GOPATH}/pkg/mod:/go/pkg/mod -v `pwd`:/yos -w /yos zhoujun/grpc-go:1.11

clean:
	rm -f bin/*

# build	
service:
	go build ${LDFLAGS} -o bin/service ./service/*.go
service-docker:
	${DOCKER_RUN} make service

build: service
build-docker:
	${DOCKER_RUN} make build