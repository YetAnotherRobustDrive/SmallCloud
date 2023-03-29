FROM node:18

WORKDIR /app

COPY package.json .

RUN npm config set legacy-peer-deps true

RUN npm install --save-dev jest 

RUN npm install redux @reduxjs/toolkit react-vis

EXPOSE 3000

COPY . .

CMD ["npm", "start"]