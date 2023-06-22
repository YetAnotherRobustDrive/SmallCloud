<img src="https://capsule-render.vercel.app/api?type=waving&color=auto&height=200&section=header&text=S-Cloud&fontSize=90" />

# S-Cloud
![S-Cloud_YARD_2조_포스터](https://github.com/YetAnotherRobustDrive/SmallCloud/assets/69972996/b38bc6c7-22e7-494c-bf21-929e44541c06)

# Roles
![image](https://github.com/YetAnotherRobustDrive/SmallCloud/assets/69972996/ce31b334-11b2-4781-a6ef-66c6f41a4743)

# Architecture
![image](https://github.com/YetAnotherRobustDrive/SmallCloud/assets/69972996/ce4685a1-8568-4900-872d-a77d5ce0cc46)

# Tech-Stack
![image](https://github.com/YetAnotherRobustDrive/SmallCloud/assets/69972996/787d4bda-e7f2-431e-9b79-4ec4331935b3)

# How to run EXE
### Encoding & Encryption are not available unless AES Crypt & FFmpeg are installed.
```bash
# download & install FFmpeg
https://ffmpeg.org/download.html
# download & install AES Crypt
https://www.aescrypt.com/download/
```

# How to run Server and Web UI
```bash
# 1. Clone this repository
git clone https://github.com/YetAnotherRobustDrive/SmallCloud

# 2. Move to the directory
cd SmallCloud

# 3. Create an env file with the following script
./make_env.pl

# 4. Run docker-compose
docker-compose up -d
```

### ENV Example List

```env
SMALLCLOUD_SECRET=491e6074d7c427d40d50198db59feacfeb53db960cb61862b5e25e970cc2a9e3
SMALLCLOUD_ORIGIN=http://localhost:3000
SMALLCLOUD_BACKEND_PORT=8000
SMALLCLOUD_FRONTEND_PORT=3000
SMALLCLOUD_ADDRESS=0.0.0.0
SMALLCLOUD_ADMIN_USERNAME=adminName
SMALLCLOUD_ADMIN_PASSWORD=adminPassword
SMALLCLOUD_DB_URL=jdbc:mariadb://db:3333/dbdb?serverTimezone=Asia/Seoul
SMALLCLOUD_DB_DATABASE=dbdb
SMALLCLOUD_DB_PORT=3333
SMALLCLOUD_DB_USERNAME=username
SMALLCLOUD_DB_PASSWORD=userpassword
SMALLCLOUD_DB_ROOT_PASSWORD=rootpassword
SMALLCLOUD_MINIO_URL=http://minio:9000
SMALLCLOUD_MINIO_PORT=9000
SMALLCLOUD_MINIO_ADMIN_PORT=9090
SMALLCLOUD_MINIO_USER=miniouser
SMALLCLOUD_MINIO_PASSWORD=miniopassword
SMALLCLOUD_MINIO_BUCKET_NAME=bucketname
```

![Footer](https://capsule-render.vercel.app/api?type=waving&color=auto&height=200&section=footer)
