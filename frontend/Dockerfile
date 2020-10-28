# ----------------------- API builder -----------------------

FROM node:15.0.1-alpine3.10 AS api-builder

WORKDIR /project/api

# Cache any dependencies
COPY ./api/package.json ./api/yarn.lock ./
RUN yarn

# Generate the API
COPY ./api .
RUN yarn generate

# ----------------------- Frontend builder -----------------------

FROM node:15.0.1-alpine3.10 AS frontend-builder

WORKDIR /project

# Cache frontend dependencies
COPY ./frontend/package.json ./frontend/yarn.lock ./frontend/
RUN cd frontend && yarn

# Build and bundle the frontend
COPY . .
COPY --from=api-builder /project/frontend/src/app/api ./frontend/src/app/api
RUN cd frontend && yarn build

# ----------------------- Final image -----------------------

FROM node:15.0.1-alpine3.10 AS iaa-frontend

WORKDIR /app
RUN yarn global add serve

COPY --from=frontend-builder /project/frontend/dist/frontend/* /app/

EXPOSE 5000
CMD serve
