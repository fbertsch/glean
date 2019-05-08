var N=null,E="",T="t",U="u",searchIndex={};
var R=["glean_core","commonmetricdata","database","string","category","jsonvalue","storage","snapshot","result","try_from","borrow_mut","try_into","type_id","glean_core::metrics","borrow","typeid","to_owned","clone_into","glean_core::ping","glean_core::storage","lifetime","default","formatter","CommonMetricData","Lifetime","ErrorType","BooleanMetric","CounterMetric","StringMetric","StringListMetric","UuidMetric","PingMaker","StorageManager","glean_ffi","ffistr","externerror"];
searchIndex[R[33]]={"doc":E,"i":[[5,"glean_initialize",R[33],E,N,[[[R[34]],[R[34]]],["u64"]]],[5,"glean_is_initialized",E,E,N,[[["u64"]],["u8"]]],[5,"glean_is_upload_enabled",E,E,N,[[["u64"]],["u8"]]],[5,"glean_set_upload_enabled",E,E,N,[[["u64"],["u8"]]]],[5,"glean_new_boolean_metric",E,E,N,[[[R[34]],[R[34]],[R[35]]],["u64"]]],[5,"glean_new_string_metric",E,E,N,[[[R[34]],[R[34]],[R[35]]],["u64"]]],[5,"glean_new_counter_metric",E,E,N,[[[R[34]],[R[34]],[R[35]]],["u64"]]],[5,"glean_counter_add",E,E,N,[[["u64"],["u64"],["u64"],[R[35]]]]],[5,"glean_boolean_set",E,E,N,[[["u64"],["u64"],["u8"],[R[35]]]]],[5,"glean_string_set",E,E,N,[[["u64"],["u64"],[R[34]],[R[35]]]]],[5,"glean_ping_collect",E,E,N,N],[5,"glean_destroy_glean",E,E,N,[[["u64"],[R[35]]]]],[5,"glean_destroy_boolean_metric",E,E,N,[[["u64"],[R[35]]]]],[5,"glean_destroy_string_metric",E,E,N,[[["u64"],[R[35]]]]],[5,"glean_destroy_counter_metric",E,E,N,[[["u64"],[R[35]]]]],[5,"glean_str_free",E,"Public destructor for strings managed by the other side of…",N,N]],"p":[]};
searchIndex[R[0]]={"doc":E,"i":[[3,R[23],R[0],E,N,N],[12,"name",E,E,0,N],[12,R[4],E,E,0,N],[12,"send_in_pings",E,E,0,N],[12,R[20],E,E,0,N],[12,"disabled",E,E,0,N],[3,"Glean",E,E,N,N],[4,R[24],E,E,N,N],[13,"Ping",E,"The metric is reset with each sent ping",1,N],[13,"Application",E,"The metric is reset on application restart",1,N],[13,"User",E,"The metric is reset with each user profile",1,N],[4,R[25],E,E,N,N],[13,"InvalidValue",E,E,2,N],[13,"InvalidLabel",E,E,2,N],[11,"as_str",E,E,1,[[["self"]],["str"]]],[11,"identifier",E,E,0,[[["self"]],[R[3]]]],[11,"should_record",E,E,0,[[["self"]],["bool"]]],[11,"storage_names",E,E,0,N],[11,"to_string",E,E,2,[[["self"]],["str"]]],[0,"metrics",E,E,N,N],[3,R[26],R[13],E,N,N],[3,R[27],E,E,N,N],[3,R[28],E,E,N,N],[3,R[29],E,E,N,N],[3,R[30],E,E,N,N],[4,"Metric",E,E,N,N],[13,"String",E,E,3,N],[13,"Boolean",E,E,3,N],[13,"Counter",E,E,3,N],[13,"Uuid",E,E,3,N],[13,"StringList",E,E,3,N],[11,"new",E,E,4,[[[R[1]]],["self"]]],[11,"set",E,E,4,[[["self"],[R[2]],["bool"]]]],[11,"new",E,E,5,[[[R[1]]],["self"]]],[11,"add",E,E,5,[[["self"],[R[2]],["u64"]]]],[11,"new",E,E,6,[[[R[1]]],["self"]]],[11,"set",E,E,6,[[["self"],[R[2]],["s"]]]],[11,"new",E,E,7,[[[R[1]]],["self"]]],[11,"add",E,E,7,[[["self"],[R[2]],["s"]]]],[11,"set",E,E,7,[[["self"],[R[2]],["vec",[R[3]]]]]],[11,"new",E,E,8,[[[R[1]]],["self"]]],[11,"set",E,E,8,[[["self"],[R[2]],["uuid"]]]],[11,"generate",E,E,8,[[["self"],[R[2]]],["uuid"]]],[11,"generate_if_missing",E,E,8,[[["self"],[R[2]]]]],[11,R[4],E,E,3,[[["self"]],["str"]]],[11,"as_json",E,E,3,[[["self"]],[R[5]]]],[0,"ping",R[0],E,N,N],[3,R[31],R[18],E,N,N],[11,"new",E,E,9,[[],["self"]]],[11,"collect",E,E,9,[[["self"],[R[2]],["str"]],[R[5]]]],[11,"collect_string",E,E,9,[[["self"],[R[2]],["str"]],[R[3]]]],[11,"store_ping",E,"Store a ping to disk in the pings directory.",9,[[["self"],["str"],["path"],["str"],["str"]],[R[8]]]],[0,R[6],R[0],E,N,N],[3,R[32],R[19],E,N,N],[11,R[7],E,E,10,[[["self"],[R[2]],["str"],["bool"]],[R[3]]]],[11,"snapshot_as_json",E,E,10,[[["self"],[R[2]],["str"],["bool"]],[R[5]]]],[11,"new",R[0],E,11,[[],["self"]]],[11,"initialize",E,"Initialize the global Glean object.",11,[[["self"],["str"],["str"]]]],[11,"is_initialized",E,"Determine whether the global Glean object is fully…",11,[[["self"]],["bool"]]],[11,"set_upload_enabled",E,"Set whether upload is enabled or not.",11,[[["self"],["bool"]]]],[11,"is_upload_enabled",E,"Determine whether upload is enabled.",11,[[["self"]],["bool"]]],[11,"get_application_id",E,E,11,[[["self"]],["str"]]],[11,"get_data_path",E,E,11,[[["self"]],["pathbuf"]]],[11,R[6],E,E,11,[[["self"]],[R[2]]]],[11,R[7],E,E,11,[[["self"],["str"],["bool"]],[R[3]]]],[11,"send_ping",E,"Send a ping by name.",11,[[["self"],["str"]],[R[8]]]],[11,"from",E,E,0,[[[T]],[T]]],[11,"into",E,E,0,[[["self"]],[U]]],[11,R[9],E,E,0,[[[U]],[R[8]]]],[11,R[14],E,E,0,[[["self"]],[T]]],[11,R[12],E,E,0,[[["self"]],[R[15]]]],[11,R[10],E,E,0,[[["self"]],[T]]],[11,R[11],E,E,0,[[["self"]],[R[8]]]],[11,"from",E,E,11,[[[T]],[T]]],[11,"into",E,E,11,[[["self"]],[U]]],[11,R[9],E,E,11,[[[U]],[R[8]]]],[11,R[14],E,E,11,[[["self"]],[T]]],[11,R[12],E,E,11,[[["self"]],[R[15]]]],[11,R[10],E,E,11,[[["self"]],[T]]],[11,R[11],E,E,11,[[["self"]],[R[8]]]],[11,"from",E,E,1,[[[T]],[T]]],[11,"into",E,E,1,[[["self"]],[U]]],[11,R[16],E,E,1,[[["self"]],[T]]],[11,R[17],E,E,1,N],[11,R[9],E,E,1,[[[U]],[R[8]]]],[11,R[14],E,E,1,[[["self"]],[T]]],[11,R[12],E,E,1,[[["self"]],[R[15]]]],[11,R[10],E,E,1,[[["self"]],[T]]],[11,R[11],E,E,1,[[["self"]],[R[8]]]],[11,"from",E,E,2,[[[T]],[T]]],[11,"into",E,E,2,[[["self"]],[U]]],[11,R[9],E,E,2,[[[U]],[R[8]]]],[11,R[14],E,E,2,[[["self"]],[T]]],[11,R[12],E,E,2,[[["self"]],[R[15]]]],[11,R[10],E,E,2,[[["self"]],[T]]],[11,R[11],E,E,2,[[["self"]],[R[8]]]],[11,"from",R[13],E,4,[[[T]],[T]]],[11,"into",E,E,4,[[["self"]],[U]]],[11,R[9],E,E,4,[[[U]],[R[8]]]],[11,R[14],E,E,4,[[["self"]],[T]]],[11,R[12],E,E,4,[[["self"]],[R[15]]]],[11,R[10],E,E,4,[[["self"]],[T]]],[11,R[11],E,E,4,[[["self"]],[R[8]]]],[11,"from",E,E,5,[[[T]],[T]]],[11,"into",E,E,5,[[["self"]],[U]]],[11,R[9],E,E,5,[[[U]],[R[8]]]],[11,R[14],E,E,5,[[["self"]],[T]]],[11,R[12],E,E,5,[[["self"]],[R[15]]]],[11,R[10],E,E,5,[[["self"]],[T]]],[11,R[11],E,E,5,[[["self"]],[R[8]]]],[11,"from",E,E,6,[[[T]],[T]]],[11,"into",E,E,6,[[["self"]],[U]]],[11,R[9],E,E,6,[[[U]],[R[8]]]],[11,R[14],E,E,6,[[["self"]],[T]]],[11,R[12],E,E,6,[[["self"]],[R[15]]]],[11,R[10],E,E,6,[[["self"]],[T]]],[11,R[11],E,E,6,[[["self"]],[R[8]]]],[11,"from",E,E,7,[[[T]],[T]]],[11,"into",E,E,7,[[["self"]],[U]]],[11,R[9],E,E,7,[[[U]],[R[8]]]],[11,R[14],E,E,7,[[["self"]],[T]]],[11,R[12],E,E,7,[[["self"]],[R[15]]]],[11,R[10],E,E,7,[[["self"]],[T]]],[11,R[11],E,E,7,[[["self"]],[R[8]]]],[11,"from",E,E,8,[[[T]],[T]]],[11,"into",E,E,8,[[["self"]],[U]]],[11,R[9],E,E,8,[[[U]],[R[8]]]],[11,R[14],E,E,8,[[["self"]],[T]]],[11,R[12],E,E,8,[[["self"]],[R[15]]]],[11,R[10],E,E,8,[[["self"]],[T]]],[11,R[11],E,E,8,[[["self"]],[R[8]]]],[11,"from",E,E,3,[[[T]],[T]]],[11,"into",E,E,3,[[["self"]],[U]]],[11,R[16],E,E,3,[[["self"]],[T]]],[11,R[17],E,E,3,N],[11,R[9],E,E,3,[[[U]],[R[8]]]],[11,R[14],E,E,3,[[["self"]],[T]]],[11,R[12],E,E,3,[[["self"]],[R[15]]]],[11,R[10],E,E,3,[[["self"]],[T]]],[11,R[11],E,E,3,[[["self"]],[R[8]]]],[11,"to_bytes",E,E,3,[[["self"]],[R[8],["vec","dataerror"]]]],[11,"from",R[18],E,9,[[[T]],[T]]],[11,"into",E,E,9,[[["self"]],[U]]],[11,R[9],E,E,9,[[[U]],[R[8]]]],[11,R[14],E,E,9,[[["self"]],[T]]],[11,R[12],E,E,9,[[["self"]],[R[15]]]],[11,R[10],E,E,9,[[["self"]],[T]]],[11,R[11],E,E,9,[[["self"]],[R[8]]]],[11,"from",R[19],E,10,[[[T]],[T]]],[11,"into",E,E,10,[[["self"]],[U]]],[11,R[9],E,E,10,[[[U]],[R[8]]]],[11,R[14],E,E,10,[[["self"]],[T]]],[11,R[12],E,E,10,[[["self"]],[R[15]]]],[11,R[10],E,E,10,[[["self"]],[T]]],[11,R[11],E,E,10,[[["self"]],[R[8]]]],[11,"eq",R[0],E,1,[[["self"],[R[20]]],["bool"]]],[11,R[21],E,E,1,[[],["self"]]],[11,R[21],E,E,0,[[],[R[1]]]],[11,R[21],R[18],E,9,[[],["self"]]],[11,"clone",R[0],E,1,[[["self"]],[R[20]]]],[11,"clone",R[13],E,3,[[["self"]],["metric"]]],[11,"fmt",R[0],E,1,[[["self"],[R[22]]],[R[8]]]],[11,"fmt",E,E,0,[[["self"],[R[22]]],[R[8]]]],[11,"fmt",E,E,2,[[["self"],[R[22]]],[R[8]]]],[11,"fmt",R[13],E,4,[[["self"],[R[22]]],[R[8]]]],[11,"fmt",E,E,5,[[["self"],[R[22]]],[R[8]]]],[11,"fmt",E,E,6,[[["self"],[R[22]]],[R[8]]]],[11,"fmt",E,E,7,[[["self"],[R[22]]],[R[8]]]],[11,"fmt",E,E,8,[[["self"],[R[22]]],[R[8]]]],[11,"fmt",E,E,3,[[["self"],[R[22]]],[R[8]]]],[11,"fmt",R[0],E,11,[[["self"],[R[22]]],[R[8]]]],[11,"serialize",R[13],E,3,[[["self"],["__s"]],[R[8]]]],[11,"deserialize",E,E,3,[[["__d"]],[R[8]]]]],"p":[[3,R[23]],[4,R[24]],[4,R[25]],[4,"Metric"],[3,R[26]],[3,R[27]],[3,R[28]],[3,R[29]],[3,R[30]],[3,R[31]],[3,R[32]],[3,"Glean"]]};
initSearch(searchIndex);addSearchOptions(searchIndex);