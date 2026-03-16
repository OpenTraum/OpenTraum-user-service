#!/bin/zsh
set -e

REPO="OpenTraum/OpenTraum-user-service"
BASE_DIR="/Users/yangjeong-u/GitHub/OpenTraum/OpenTraum-user-service"
PARENT_SHA="a70f39791e1430c06ddc3072efb208305f94a3c9"

# Get the base tree
BASE_TREE=$(gh api "repos/$REPO/git/commits/$PARENT_SHA" --jq '.tree.sha')
echo "Base tree: $BASE_TREE"

# Files to commit (relative paths)
FILES=(
  "build.gradle"
  "settings.gradle"
  "gradle/wrapper/gradle-wrapper.properties"
  ".gitignore"
  "Dockerfile"
  "src/main/java/com/opentraum/user/UserServiceApplication.java"
  "src/main/java/com/opentraum/user/config/R2dbcConfig.java"
  "src/main/java/com/opentraum/user/config/RedisConfig.java"
  "src/main/java/com/opentraum/user/domain/entity/User.java"
  "src/main/java/com/opentraum/user/domain/entity/Tenant.java"
  "src/main/java/com/opentraum/user/domain/repository/UserRepository.java"
  "src/main/java/com/opentraum/user/domain/repository/TenantRepository.java"
  "src/main/java/com/opentraum/user/domain/dto/UserCreateRequest.java"
  "src/main/java/com/opentraum/user/domain/dto/UserResponse.java"
  "src/main/java/com/opentraum/user/domain/dto/TenantCreateRequest.java"
  "src/main/java/com/opentraum/user/domain/dto/TenantResponse.java"
  "src/main/java/com/opentraum/user/domain/service/UserService.java"
  "src/main/java/com/opentraum/user/domain/service/UserServiceImpl.java"
  "src/main/java/com/opentraum/user/domain/service/TenantService.java"
  "src/main/java/com/opentraum/user/domain/service/TenantServiceImpl.java"
  "src/main/java/com/opentraum/user/domain/controller/UserController.java"
  "src/main/java/com/opentraum/user/domain/controller/TenantController.java"
  "src/main/resources/application.yml"
  "src/main/resources/schema.sql"
)

# Create blobs and build tree entries
TREE_ENTRIES="["
FIRST=true

for FILE in "${FILES[@]}"; do
  echo "Creating blob for: $FILE"
  CONTENT=$(base64 < "$BASE_DIR/$FILE")
  BLOB_SHA=$(gh api "repos/$REPO/git/blobs" \
    -f "content=$CONTENT" \
    -f "encoding=base64" \
    --jq '.sha')
  echo "  Blob SHA: $BLOB_SHA"

  if [ "$FIRST" = true ]; then
    FIRST=false
  else
    TREE_ENTRIES+=","
  fi
  TREE_ENTRIES+="{\"path\":\"$FILE\",\"mode\":\"100644\",\"type\":\"blob\",\"sha\":\"$BLOB_SHA\"}"
done

TREE_ENTRIES+="]"

# Create tree
echo ""
echo "Creating tree..."
TREE_JSON="{\"base_tree\":\"$BASE_TREE\",\"tree\":$TREE_ENTRIES}"
echo "$TREE_JSON" > "$BASE_DIR/_tree_payload.json"
TREE_SHA=$(gh api "repos/$REPO/git/trees" --input "$BASE_DIR/_tree_payload.json" --jq '.sha')
echo "Tree SHA: $TREE_SHA"

# Create commit
echo ""
echo "Creating commit..."
COMMIT_JSON="{\"message\":\"feat: Spring Boot 보일러플레이트 설정\\n\\n- Spring Boot 3.5.10 + Java 21 + WebFlux + R2DBC 기반 프로젝트 구조 구성\\n- User/Tenant 엔티티, 리포지토리, 서비스, 컨트롤러 CRUD 구현\\n- R2DBC (PostgreSQL) 및 Redis Reactive 설정\\n- Actuator + Prometheus 모니터링 설정\\n- schema.sql DDL, Dockerfile, .gitignore 추가\",\"tree\":\"$TREE_SHA\",\"parents\":[\"$PARENT_SHA\"]}"
echo "$COMMIT_JSON" > "$BASE_DIR/_commit_payload.json"
COMMIT_SHA=$(gh api "repos/$REPO/git/commits" --input "$BASE_DIR/_commit_payload.json" --jq '.sha')
echo "Commit SHA: $COMMIT_SHA"

# Update feat/#1 branch ref
echo ""
echo "Updating feat/#1 branch..."
UPDATE_JSON="{\"sha\":\"$COMMIT_SHA\",\"force\":false}"
echo "$UPDATE_JSON" > "$BASE_DIR/_update_payload.json"
gh api "repos/$REPO/git/refs/heads/feat/%231" -X PATCH --input "$BASE_DIR/_update_payload.json"

# Clean up temp files
rm -f "$BASE_DIR/_tree_payload.json" "$BASE_DIR/_commit_payload.json" "$BASE_DIR/_update_payload.json" "$BASE_DIR/_push_files.sh"

echo ""
echo "Done! All files committed to feat/#1"
