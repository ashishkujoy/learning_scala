FROM amazonlinux:2017.03

ENV SCALA_VERSION 2.11.8
ENV SBT_VERSION 1.2.3

# Install Java8
RUN yum install -y java-1.8.0-openjdk-devel

# Install Scala and SBT
RUN yum install -y https://downloads.lightbend.com/scala/$SCALA_VERSION/scala-$SCALA_VERSION.rpm
RUN yum install -y https://dl.bintray.com/sbt/rpm/sbt-$SBT_VERSION.rpm