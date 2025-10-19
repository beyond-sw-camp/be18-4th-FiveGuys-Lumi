# Build Stage: 프론트 빌드
FROM node:20-alpine AS builder
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

# Nginx Stage: 정적 파일 + API 프록시 설정
FROM nginx:stable-alpine3.21
LABEL maintainer="amicitia <dyungwoo3600@gmail.com>"
LABEL version="1.0"

# 빌드 결과 복사
COPY --from=builder /app/dist /usr/share/nginx/html

# nginx 설정 복사 (이 부분이 핵심)
COPY default.conf /etc/nginx/conf.d/default.conf

# 환경설정
ENV TZ=Asia/Seoul
EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
