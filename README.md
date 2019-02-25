# multimodule_class
## How to build jar with dependency
$ javac -cp lib/h2-1.4.197.jar -d classes src/com/tsystems/oop/dbutil/DbUtil.java

$ jar cfm DbUtil.jar manifest.txt -C classes com

base jar will be debt_collector, others -> to lib folder
