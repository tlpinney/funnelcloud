hdfs dfs -rm -r current_nodes_tsv 
hdfs dfs -rm -r current_node_tags_tsv
hdfs dfs -rm -r current_ways_tsv
hdfs dfs -rm -r current_way_tags_tsv 
hdfs dfs -rm -r current_way_nodes_tsv
hdfs dfs -rm -r current_relations_tsv
hdfs dfs -rm -r current_relation_members_tsv
hdfs dfs -rm -r current_relation_tags_tsv

hdfs dfs -mkdir current_nodes_tsv 
hdfs dfs -mkdir current_node_tags_tsv
hdfs dfs -mkdir current_ways_tsv
hdfs dfs -mkdir current_way_tags_tsv 
hdfs dfs -mkdir current_way_nodes_tsv
hdfs dfs -mkdir current_relations_tsv
hdfs dfs -mkdir current_relation_members_tsv
hdfs dfs -mkdir current_relation_tags_tsv


hdfs dfs -put current_nodes.tsv current_nodes_tsv
hdfs dfs -put current_node_tags.tsv current_node_tags_tsv
hdfs dfs -put current_ways.tsv current_ways_tsv
hdfs dfs -put current_way_tags.tsv current_way_tags_tsv
hdfs dfs -put current_way_nodes.tsv current_way_nodes_tsv
hdfs dfs -put current_relations.tsv current_relations_tsv 
hdfs dfs -put current_relation_members.tsv current_relation_members_tsv
hdfs dfs -put current_relation_tags.tsv current_relation_tags_tsv 


