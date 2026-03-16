#!/bin/zsh
set -e

REPO="OpenTraum/OpenTraum-user-service"
BD="/Users/yangjeong-u/GitHub/OpenTraum/OpenTraum-user-service"
BASE_TREE="80ad8aa7abbbbb9b381d14d6731abe2107a3ceb4"
PARENT="a70f39791e1430c06ddc3072efb208305f94a3c9"

create_blob() {
  local file="$1"
  local content=$(base64 -i "$BD/$file")
  gh api "repos/$REPO/git/blobs" -f "content=$content" -f encoding=base64 --jq '.sha'
}

echo "Creating blobs..."
B01=$(create_blob "build.gradle")
B02=$(create_blob "settings.gradle")
B03=$(create_blob "gradle/wrapper/gradle-wrapper.properties")
B04=$(create_blob ".gitignore")
B05=$(create_blob "Dockerfile")
B06=$(create_blob "src/main/java/com/opentraum/user/UserServiceApplication.java")
B07=$(create_blob "src/main/java/com/opentraum/user/config/R2dbcConfig.java")
B08=$(create_blob "src/main/java/com/opentraum/user/config/RedisConfig.java")
B09=$(create_blob "src/main/java/com/opentraum/user/domain/entity/User.java")
B10=$(create_blob "src/main/java/com/opentraum/user/domain/entity/Tenant.java")
B11=$(create_blob "src/main/java/com/opentraum/user/domain/repository/UserRepository.java")
B12=$(create_blob "src/main/java/com/opentraum/user/domain/repository/TenantRepository.java")
B13=$(create_blob "src/main/java/com/opentraum/user/domain/dto/UserCreateRequest.java")
B14=$(create_blob "src/main/java/com/opentraum/user/domain/dto/UserResponse.java")
B15=$(create_blob "src/main/java/com/opentraum/user/domain/dto/TenantCreateRequest.java")
B16=$(create_blob "src/main/java/com/opentraum/user/domain/dto/TenantResponse.java")
B17=$(create_blob "src/main/java/com/opentraum/user/domain/service/UserService.java")
B18=$(create_blob "src/main/java/com/opentraum/user/domain/service/UserServiceImpl.java")
B19=$(create_blob "src/main/java/com/opentraum/user/domain/service/TenantService.java")
B20=$(create_blob "src/main/java/com/opentraum/user/domain/service/TenantServiceImpl.java")
B21=$(create_blob "src/main/java/com/opentraum/user/domain/controller/UserController.java")
B22=$(create_blob "src/main/java/com/opentraum/user/domain/controller/TenantController.java")
B23=$(create_blob "src/main/resources/application.yml")
B24=$(create_blob "src/main/resources/schema.sql")

echo "All blobs created. Building tree..."

cat > "$BD/_tree.json" << TREEJSON
{
  "base_tree": "$BASE_TREE",
  "tree": [
    {"path":"build.gradle","mode":"100644","type":"blob","sha":"$B01"},
    {"path":"settings.gradle","mode":"100644","type":"blob","sha":"$B02"},
    {"path":"gradle/wrapper/gradle-wrapper.properties","mode":"100644","type":"blob","sha":"$B03"},
    {"path":".gitignore","mode":"100644","type":"blob","sha":"$B04"},
    {"path":"Dockerfile","mode":"100644","type":"blob","sha":"$B05"},
    {"path":"src/main/java/com/opentraum/user/UserServiceApplication.java","mode":"100644","type":"blob","sha":"$B06"},
    {"path":"src/main/java/com/opentraum/user/config/R2dbcConfig.java","mode":"100644","type":"blob","sha":"$B07"},
    {"path":"src/main/java/com/opentraum/user/config/RedisConfig.java","mode":"100644","type":"blob","sha":"$B08"},
    {"path":"src/main/java/com/opentraum/user/domain/entity/User.java","mode":"100644","type":"blob","sha":"$B09"},
    {"path":"src/main/java/com/opentraum/user/domain/entity/Tenant.java","mode":"100644","type":"blob","sha":"$B10"},
    {"path":"src/main/java/com/opentraum/user/domain/repository/UserRepository.java","mode":"100644","type":"blob","sha":"$B11"},
    {"path":"src/main/java/com/opentraum/user/domain/repository/TenantRepository.java","mode":"100644","type":"blob","sha":"$B12"},
    {"path":"src/main/java/com/opentraum/user/domain/dto/UserCreateRequest.java","mode":"100644","type":"blob","sha":"$B13"},
    {"path":"src/main/java/com/opentraum/user/domain/dto/UserResponse.java","mode":"100644","type":"blob","sha":"$B14"},
    {"path":"src/main/java/com/opentraum/user/domain/dto/TenantCreateRequest.java","mode":"100644","type":"blob","sha":"$B15"},
    {"path":"src/main/java/com/opentraum/user/domain/dto/TenantResponse.java","mode":"100644","type":"blob","sha":"$B16"},
    {"path":"src/main/java/com/opentraum/user/domain/service/UserService.java","mode":"100644","type":"blob","sha":"$B17"},
    {"path":"src/main/java/com/opentraum/user/domain/service/UserServiceImpl.java","mode":"100644","type":"blob","sha":"$B18"},
    {"path":"src/main/java/com/opentraum/user/domain/service/TenantService.java","mode":"100644","type":"blob","sha":"$B19"},
    {"path":"src/main/java/com/opentraum/user/domain/service/TenantServiceImpl.java","mode":"100644","type":"blob","sha":"$B20"},
    {"path":"src/main/java/com/opentraum/user/domain/controller/UserController.java","mode":"100644","type":"blob","sha":"$B21"},
    {"path":"src/main/java/com/opentraum/user/domain/controller/TenantController.java","mode":"100644","type":"blob","sha":"$B22"},
    {"path":"src/main/resources/application.yml","mode":"100644","type":"blob","sha":"$B23"},
    {"path":"src/main/resources/schema.sql","mode":"100644","type":"blob","sha":"$B24"}
  ]
}
TREEJSON

TREE_SHA=$(gh api "repos/$REPO/git/trees" --input "$BD/_tree.json" --jq '.sha')
echo "Tree SHA: $TREE_SHA"

cat > "$BD/_commit.json" << COMMITJSON
{
  "message": "feat: Spring Boot 보일러플레이트 설정\n\n- Spring Boot 3.5.10 + Java 21 + WebFlux + R2DBC 기반 프로젝트 구조 구성\n- User/Tenant 엔티티, 리포지토리, 서비스, 컨트롤러 CRUD 구현\n- R2DBC (PostgreSQL) 및 Redis Reactive 설정\n- Actuator + Prometheus 모니터링 설정\n- schema.sql DDL, Dockerfile, .gitignore 추가",
  "tree": "$TREE_SHA",
  "parents": ["$PARENT"]
}
COMMITJSON

COMMIT_SHA=$(gh api "repos/$REPO/git/commits" --input "$BD/_commit.json" --jq '.sha')
echo "Commit SHA: $COMMIT_SHA"

cat > "$BD/_update_ref.json" << UPDATEJSON
{
  "sha": "$COMMIT_SHA",
  "force": false
}
UPDATEJSON

gh api "repos/$REPO/git/refs/heads/feat/%231" -X PATCH --input "$BD/_update_ref.json" --jq '.object.sha'
echo "Branch updated!"

rm -f "$BD/_tree.json" "$BD/_commit.json" "$BD/_update_ref.json" "$BD/_commit.sh"
echo "Done!"
