FROM node:18

WORKDIR /app

COPY package.json .

RUN npm config set legacy-peer-deps true

RUN npm install

RUN npm install --save-dev jest

RUN npm install redux

RUN npm install @reduxjs/toolkit

EXPOSE 3000

COPY . .

CMD ["npm", "start"]