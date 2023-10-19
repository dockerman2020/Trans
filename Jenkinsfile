pipeline {
    agent {
        kubernetes {
            yaml '''
apiVersion: v1
kind: Pod
spec:
  containers:
  - name: cms
    #image: python:3.9.17-slim
    image: dockerman2002/cms-dev:v1.0.5@sha256:77c303a98504b6b4c31a81be9dcc887fbedb255fb09c3376baf847838b47c4c2
    command:
    - sleep
    args:
    - infinity
  - name: scanner
    image: dockerman2002/e2e-dev:0.41.1@sha256:9ed154e6e1d737dfe28b3ff063f46e4fbdc15106dbe0b6c2c26e10c8b1e82837
    command:
    - cat
    tty: true
    volumeMounts:
    - name: docker-socket
      mountPath: /var/run/docker.sock
  - name: cms-docker
    image: alpinelinux/docker-cli
    command:
    - cat
    tty: true
    volumeMounts:
    - name: docker-socket
      mountPath: /var/run/docker.sock
  volumes:
  - name: docker-socket
    hostPath:
      path: /var/run/docker.sock
      type: Socket
'''
            defaultContainer 'cms'
            idleMinutes 1
            namespace 'jenkins-worker'
        }
    }
// tools {
//         jdk 'java17'
//     }
environment {
    APP_NAME = "cms-django"
    RELEASE = "1.0.0"
    DOCKER_USER = "dockerman2002"
    DOCKER_PASS = 'dockerhub'
    IMAGE_NAME = "${DOCKER_USER}" + "/" + "${APP_NAME}"
    IMAGE_TAG = "${RELEASE}-${BUILD_NUMBER}"
    CMS_API_TOKEN = credentials('CMS_API_TOKEN')
    TRIVY_API_TOKEN = credentials('trivy-token')
    TRIVY_SERVER = "trivy.trivy-redis.svc.cluster.local"
    JENKINS_URL = "jenkins.jenkins.svc.cluster.local"
    TRIVY_PORT = "4954"
    SONAR_CMS = credentials('jenkins-sonar-cms')
    SLACK_BOT_TOKEN = credentials('SLACK_BOT_TOKEN')
    SLACK_CHANNEL_ID = credentials('SLACK_CHANNEL_ID')
    SONARQUBE_HOME = tool 'sonarqube-scanner'
    GIT_BRANCH = "feature/test01"
    }
options {
  buildDiscarder logRotator(artifactDaysToKeepStr: '5', artifactNumToKeepStr: '2', daysToKeepStr: '5', numToKeepStr: '3')
    }
  stages {
    stage("Cleanup Workspace"){
        steps {
            cleanWs()
        }
    }
    stage("Checkout from SCM"){
        steps {
            container('cms-docker') {
            git branch: "${GIT_BRANCH}", credentialsId: 'cms', url: 'https://github.com/dockerman2020/cms.git'
            }
        }
    }
    stage("Transform Yaml"){
        steps {
          container('cms-docker') {
          sh '''
          #!/bin/bash
          pwd
          ls -lrt
          export yamlfile="myyaml.yaml"
          export IND=$(awk -F':' '/^    jkcd/{print $1}' $yamlfile)
          export INDv=$(awk -F':' '/^    jkcd/{print $2}' $yamlfile)

          awk -v JK="$IND" -v JKv="$INDv" '
          $0 ~ JK ": " JKv {
          print
          print JK "-1: " JKv
          next
          }
          { print }
          ' $yamlfile
          '''
        }
      }
    }
  }
}