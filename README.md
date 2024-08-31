# Earth CPR

이 저장소는 Earth CPR 프로젝트의 소스 코드를 포함하고 있습니다. GitHub Actions와 Docker를 사용해 CI/CD 환경을 구축하여 AWS에 자동으로 배포되도록 설정했습니다. 메인 브랜치에 커밋이 발생하면, 애플리케이션이 자동으로 빌드되고 AWS에 배포됩니다.

## CI/CD 파이프라인

CI/CD 파이프라인은 다음과 같은 기술을 사용하여 구축되었습니다:
- **GitHub Actions**: 빌드, 테스트, 배포 프로세스를 자동화합니다.
- **Docker**: 애플리케이션을 컨테이너화하여 일관되고 신뢰할 수 있는 배포를 보장합니다.
- **AWS**: 애플리케이션을 호스팅하여 확장 가능하고 안전한 환경을 제공합니다.

### 배포 프로세스

1. **메인 브랜치에 커밋**: `main` 브랜치에 푸시된 커밋은 CI/CD 파이프라인을 트리거합니다.
2. **빌드 및 테스트**: Gradle을 사용하여 애플리케이션이 빌드되고, 모든 테스트가 실행되어 코드 품질을 보장합니다.
3. **Docker 이미지 생성**: 애플리케이션의 Docker 이미지가 생성되고 컨테이너 레지스트리에 푸시됩니다.
4. **AWS 배포**: 새로운 Docker 이미지가 AWS EC2에 배포되어 실행 중인 서비스가 업데이트됩니다.

### 로컬 환경에서 애플리케이션 빌드 및 실행

```bash
./gradlew clean build
java -jar build/libs/earthcpr-0.0.1-SNAPSHOT.jar
```

### API Docs
[EarthCPR API Document]([링크](http://ec2-3-34-227-48.ap-northeast-2.compute.amazonaws.com:8080/))
