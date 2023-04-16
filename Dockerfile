FROM node:18

WORKDIR /app

COPY package.json .

RUN npm install

RUN npm config set legacy-peer-deps true

EXPOSE 3000

COPY . .

CMD ["npm", "start"]