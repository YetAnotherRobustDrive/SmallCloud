docker stop smallcloud-frontend; 
docker build -t frontend . && docker run -v $(pwd)/src:/app/src:ro -p 3000:3000 -d --rm --name=frontend smallcloud-frontend
