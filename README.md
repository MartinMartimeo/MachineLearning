KMeans
===============
java -jar "dist/KMeans.jar"

Implementation of KMeans on the car.data set.


The implementation follows the strcuture of data retriving -> clustering  -> result printed.
For the attributes of the dataset a total order is provided in a common sense, meaning low < med < high. You can have a look at cardata/CarFieldComparator for that. The data itself is safed in a pythonic way as a list of dictionaries (Data -> Field), with providing some easy usage function in the data package.
Using the implemented methods the algorithm itself is straigh forward (kmeans/KMeans). The will be k cluster created (initCluster), for each cluster a point of the dataset will be used as initalisation for the center point. Afterwards for each data element the neares cluster point will be calculated (getClusterFields), based on the points that consirn the cluster the new center will be caclulated (updateCenteroid) and the last 2 steps will be looped until the center points do not move anymore.
On testing the implementation it occured that the break condition is not usefull if you have very discret field values. At a certain point the cluster get updated repeatantly jumping just around the minima. I implemented therefore a second break rule when reaching a local minima that had been reached one time before. 
Finding clusters on a dataset usefull for classification is often a poor idea. For k=4 kmeans finds 4 clusters, but each of the cluster is distingushed as "unacc", primary because of the high count of unacc fields and (probably) the issue that I nearly weighted all attributes same. This results in an error rate of 30% =^= all fields that aren't unacc. I think using and higher cluster rate and use the information from previous clusterings of the car.data how to weight the attributes properly will result in a lower error rate.



