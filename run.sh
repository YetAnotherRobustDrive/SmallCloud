docker stop yard_test; docker build -t yard_test_image . && docker run -v $(pwd)/src:/app/src:ro -p 3000:3000 -d --rm --name=yard_test yard_test_image
