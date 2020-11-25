// Tell Jenkins how to build projects from this repository
pipeline {
	agent {
		label 'magicdraw19'
	} 
	parameters {
		string(name: 'RELEASE_VERSION', defaultValue: '', 
			description: 'Set this parameter to update the project version version accordingly, should be used to remove -SNAPSHOT from version numbers. Leave it empty to keep version from the repository.')
	}
	// Keep only the last 5 builds
	options {
		buildDiscarder(logRotator(numToKeepStr: '5'))
	}
	environment {
		VERSION_STRINGS = " ${params.RELEASE_VERSION ? '-Pversion=' + params.RELEASE_VERSION : ''} "
	}
	
	tools { 
		maven 'Maven 3.3.9' 
		jdk 'OpenJDK 8' 
	}

	stages {
		stage('Build SoS Deployment Plug-in') { 
			steps {
				withCredentials([usernamePassword(credentialsId: 'nexus-buildserver-deploy', passwordVariable: 'DEPLOY_PASSWORD', usernameVariable: 'DEPLOY_USER')]) {
					dir ('./SoS Deployment Plugin/'){
						sh "./gradlew clean"
						sh "./gradlew ${VERSION_STRINGS} assemble"
					}
				}
			}
		}
		stage('Deploy SoS Deployment Plug-in') {
			when {branch "master"} 
			steps {
				withCredentials([usernamePassword(credentialsId: 'nexus-buildserver-deploy', passwordVariable: 'DEPLOY_PASSWORD', usernameVariable: 'DEPLOY_USER')]) {
					script{
					    dir ('./SoS Deployment Plugin/') {
                    			sh "./gradlew ${VERSION_STRINGS} bundle"
					    }
					}
				}
			}
		}
		stage('Build SoS Design Plug-in') { 
			steps {
				withCredentials([usernamePassword(credentialsId: 'nexus-buildserver-deploy', passwordVariable: 'DEPLOY_PASSWORD', usernameVariable: 'DEPLOY_USER')]) {
					dir ('./SoS Design Plugin/'){
						sh "./gradlew clean"
						sh "./gradlew ${VERSION_STRINGS} assemble"
					}
				}
			}
		}
		stage('Deploy SoS Design Plug-in') {
			when {branch "master"} 
			steps {
				withCredentials([usernamePassword(credentialsId: 'nexus-buildserver-deploy', passwordVariable: 'DEPLOY_PASSWORD', usernameVariable: 'DEPLOY_USER')]) {
					script{
					    dir ('./SoS Design Plugin/') {
                    			sh "./gradlew ${VERSION_STRINGS} bundle"
					    }
					}
				}
			}
		}
		stage('Build Toolchain Design Plug-in') { 
			steps {
				withCredentials([usernamePassword(credentialsId: 'nexus-buildserver-deploy', passwordVariable: 'DEPLOY_PASSWORD', usernameVariable: 'DEPLOY_USER')]) {
					dir ('./Toolchain Design Plugin/'){
						sh "./gradlew clean"
						sh "./gradlew ${VERSION_STRINGS} assemble"
					}
				}
			}
		}
		stage('Deploy Toolchain Design Plug-in') {
			when {branch "master"} 
			steps {
				withCredentials([usernamePassword(credentialsId: 'nexus-buildserver-deploy', passwordVariable: 'DEPLOY_PASSWORD', usernameVariable: 'DEPLOY_USER')]) {
					script{
					    dir ('./Toolchain Design Plugin/') {
                    			sh "./gradlew ${VERSION_STRINGS} bundle"
					    }
					}
				}
			}
		}
	}

	post {
		always {
			archiveArtifacts artifacts: 'SoS Deployment Plugin/build/bundles/arrowhead-vorto-extension-magicdraw-plugin.zip', onlyIfSuccessful: true
			archiveArtifacts artifacts: 'SoS Design Plugin/build/bundles/arrowhead-magicdraw-plugin.zip', onlyIfSuccessful: true
			archiveArtifacts artifacts: 'Toolchain Design Plugin/build/bundles/arrowhead-tools-magicdraw-plugin.zip', onlyIfSuccessful: true
		}
	}
}

