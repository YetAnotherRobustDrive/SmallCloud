FROM node:18

WORKDIR /app

COPY package.json .

RUN npm config set legacy-peer-deps true

RUN npm install

EXPOSE 3000

COPY . .

CMD ["npm", "run", "start"]

#docker stop yard_test; docker build -t yard_test_image . && docker run -v $(pwd)/src:/app/src:ro -p 3000:3000 -d --rm --name=yard_test yard_test_image