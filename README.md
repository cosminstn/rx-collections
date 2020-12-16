## About
This library offers a set of collections developed with reactive principles. 

## Flowable

Example taken from [stackoverflow response](https://stackoverflow.com/a/42186906/8520278).
```java
PublishProcessor<Integer> pp = PublishProcessor.create();
Flowable<Integer> out = pp.onBackpressureLatest();
```


## Use cases

## Drawbacks

## Future TODOs 
- [x] Implement for `java.util.HashMap`
- [x] Implement test for RxHashMap
- [x] Implement for `java.util.TreeMap`
- [ ] Implement tests for RxTreeMap
- [ ] Implement for `java.util.ArrayList`  
- [ ] Implement for `java.util.HashSet`
- [x] Implement for `java.util.TreeSet`
- [ ] Look further into ways of implementing a watcher for object property changes.
- [ ] [Publish the library to maven central](https://www.albertgao.xyz/2018/01/18/how-to-publish-artifact-to-maven-central-via-gradle/)
