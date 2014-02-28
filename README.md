simple-dynamic-time-warping-java
================================

Simple dynamic time warping in Java with sample data and sample programs

API should be pretty flexible. Sample data compares spoken digits using several 
different features. 

Problem 6 attempts to match sample points from training data to other training points using
ONLY intensity feature and not suprisingly does not yield good results.

Problem 7 attempts to match sample points from training data to other training points using
ALL intensity features and gets fantastic results.

Problem 8 uses testing data against training data and all features and gets perfect results also.

All data is cached in memory so for sure you could run into some memory issues if in tiny device
or if you use your own data. Feel free to fix that :)
