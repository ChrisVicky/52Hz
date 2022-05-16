docker stop Hz
docker rm Hz
docker rmi hz:latest

path=`pwd`

echo docker build -t hz .
docker build -t hz .
echo docker run -d --name Hz  -p 7070:7070 hz
docker run -d --name Hz  -p 7070:7070 hz
