FROM node:18

WORKDIR /app

RUN npm install --save-dev jest 

RUN npm install react-icons --save

RUN npm install redux @reduxjs/toolkit react-vis

COPY package.json .

RUN npm config set legacy-peer-deps true

EXPOSE 3000

COPY . .

CMD ["npm", "start"]