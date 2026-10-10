# Use nginx to serve the frontend
FROM nginx:alpine

# Copy frontend files to nginx
COPY . /usr/share/nginx/html/

# Copy nginx configuration (from current directory since this Dockerfile is in frontend/)
COPY nginx.conf /etc/nginx/nginx.conf

EXPOSE 8080

CMD ["nginx", "-g", "daemon off;"]
