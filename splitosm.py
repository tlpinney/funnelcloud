import fileformat_pb2 
import osmformat_pb2 
from struct import unpack
from zlib import decompress 
import sys


# Prototype Python program to create "splits" of osm for Hadoop processing 
# License: Apache 2.0, Copyright Travis L Pinney


# Keep track of file index 
index = 0

bh = fileformat_pb2.BlobHeader()
b = fileformat_pb2.Blob()

f = open("district-of-columbia-latest.osm.pbf","rb")

# read the first 4 bytes to get size of the BlobHeader
bh_len = unpack("!i", f.read(4))[0]
index += 4

# read the BlobHeader 
bh.ParseFromString(f.read(bh_len))
index += bh_len 

print >> sys.stderr, bh

# Read the Blob 
b.ParseFromString(f.read(bh.datasize))
index += bh.datasize


print >> sys.stderr, b

# Assuming first "Blob" is a OSMHeader
if bh.type == "OSMHeader":
  hb = osmformat_pb2.HeaderBlock()
  hb.ParseFromString(decompress(b.zlib_data))
  print >> sys.stderr, hb 


# just care where the OSMData is for each blob, to create the splits for hadoop
# this creates a tab delimited output of start of OSMData and length of OSMData
while 1: 
  data = f.read(4)
  if data == "":
    break
  bh_len = unpack("!i", data)[0]
  index += bh_len
  bh.ParseFromString(f.read(bh_len))
  b.ParseFromString(f.read(bh.datasize))
  print "%s\t%s" % (index, bh.datasize)
  index += bh.datasize







