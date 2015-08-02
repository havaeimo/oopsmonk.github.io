#!/usr/bin/env python
#coding=utf-8

import sys
import contextlib
import argparse
import geocoder

import matplotlib
# Anti-Grain Geometry (AGG) backend so PyGeoIpMap can be used 'headless'
matplotlib.use('Agg')
import matplotlib.pyplot as plt
from mpl_toolkits.basemap import Basemap

def get_ip(ip_file):
    """
    Returns a list of IP addresses from a file containing one IP per line.
    """
    with contextlib.closing(ip_file):
        return [line.strip() for line in ip_file]


def getgeo_google(ip_list):
    count=1
    latlng_set=[]
    for ip in ip_list:
        geoinfo = geocoder.google(ip)
        print("[google] {} ({}/{}) country: {}".format(ip, count, len(ip_list), geoinfo.json.get('country')))
        if geoinfo.latlng:
            latlng_set.append([ip, geoinfo.latlng[0], geoinfo.latlng[1], geoinfo.json.get('country')])
        else:
            print("{} not found".format(ip))
            latlng_set.append([ip, "","", geoinfo.json.get('country')])
        count = count + 1
    #print(latlng_set)
    return latlng_set 

def getgeo_osm(ip_list):
    count=1
    latlng_set=[]
    for ip in ip_list:
        geoinfo = geocoder.osm(ip)
        print("[osm] {} ({}/{}) country: {}".format(ip, count, len(ip_list), geoinfo.json.get('country')))
        if geoinfo.latlng:
            latlng_set.append([ip, geoinfo.latlng[0], geoinfo.latlng[1]])
        else:
            print("{} not found".format(ip))
            latlng_set.append([ip, "","" ])
        count = count + 1
    #print(latlng_set)
    return latlng_set 

def getgeo_maxmind(ip_list):
    count=1
    latlng_set=[]
    for ip in ip_list:
        geoinfo = geocoder.maxmind(ip)
        print("[maxmind] {} ({}/{}) country: {}".format(ip, count, len(ip_list), geoinfo.json.get('country')))
        if geoinfo.latlng:
            latlng_set.append([ip, geoinfo.latlng[0], geoinfo.latlng[1]])
        else:
            print("{} not found".format(ip))
            latlng_set.append([ip, "","" ])
        count = count + 1
    #print(latlng_set)
    return latlng_set 

def getgeo_tomtom(ip_list):
    count=1
    latlng_set=[]
    for ip in ip_list:
        geoinfo = geocoder.tomtom(ip)
        print("[tomtom] {} ({}/{}) country: {}".format(ip, count, len(ip_list), geoinfo.json.get('country')))
        if geoinfo.latlng:
            latlng_set.append([ip, geoinfo.latlng[0], geoinfo.latlng[1]])
        else:
            print("{} not found".format(ip))
            latlng_set.append([ip, "","" ])
        count = count + 1
    #print(latlng_set)
    return latlng_set 

def getgeo_yahoo(ip_list):
    count=1
    latlng_set=[]
    for ip in ip_list:
        geoinfo = geocoder.yahoo(ip)
        print("[yahoo] {} ({}/{}) country: {}".format(ip, count, len(ip_list), geoinfo.json.get('country')))
        if geoinfo.latlng:
            latlng_set.append([ip, geoinfo.latlng[0], geoinfo.latlng[1]])
        else:
            print("{} not found".format(ip))
            latlng_set.append([ip, "","" ])
        count = count + 1
    #print(latlng_set)
    return latlng_set 

def getgeo_mapquest(ip_list):
    count=1
    latlng_set=[]
    for ip in ip_list:
        geoinfo = geocoder.mapquest(ip)
        print("[mapquest] {} ({}/{}) country: {}".format(ip, count, len(ip_list), geoinfo.json.get('country')))
        if geoinfo.latlng:
            latlng_set.append([ip, geoinfo.latlng[0], geoinfo.latlng[1]])
        else:
            print("{} not found".format(ip))
            latlng_set.append([ip, "","" ])
        count = count + 1
    #print(latlng_set)
    return latlng_set 

def generate_map(output, latlng, wesn=None):
    """
    Using Basemap and the matplotlib toolkit, this function generates a map and
    puts a red dot at the location of every IP addresses found in the list.
    The map is then saved in the file specified in `output`.
    """
    print("Generating map and saving it to {}".format(output))
    lats=[]
    lngs=[]
    for i in latlng:
        if i[1]:
            lats.append(i[1])
            lngs.append(i[2])

    if wesn:
        wesn = [float(i) for i in wesn.split('/')]
        m = Basemap(projection='cyl', resolution='l',
                llcrnrlon=wesn[0], llcrnrlat=wesn[2],
                urcrnrlon=wesn[1], urcrnrlat=wesn[3])
    else:
        m = Basemap(projection='cyl', resolution='l')
    m.bluemarble()
    x, y = m(lngs, lats)
    m.scatter(x, y, s=1, color='#ff0000', marker='o', alpha=0.3)
    plt.savefig(output, dpi=300, bbox_inches='tight')


def main():
    #UnicodeEncodeError: 'ascii' codec can't encode character
    reload(sys)
    sys.setdefaultencoding('utf-8')

    parser = argparse.ArgumentParser(description='Visualize community on a map.')
    parser.add_argument('-i', '--input', dest="input", type=argparse.FileType('r'),
            help='Input file. One IP per line.',
            default=sys.stdin)
    parser.add_argument('-o', '--output', default='output.png', help='Path to save the file (e.g. /tmp/output.png)')
    parser.add_argument('--extents', default=None, help='Extents for the plot (west/east/south/north). Default global.')
    args = parser.parse_args()

    output = args.output

    ip_list = get_ip(args.input)
    maxmind_geo = getgeo_maxmind(ip_list)
    generate_map("maxmind-"+output, maxmind_geo, wesn=args.extents)

    osm_geo= getgeo_osm(ip_list)
    generate_map("osm-"+output, osm_geo, wesn=args.extents)

    google_geo= getgeo_google(ip_list)
    generate_map("google-"+output, google_geo, wesn=args.extents)

    tomtom_geo= getgeo_tomtom(ip_list)
    generate_map("tomtom-"+output, tomtom_geo, wesn=args.extents)

    yahoo_geo= getgeo_yahoo(ip_list)
    generate_map("yahoo-"+output, yahoo_geo, wesn=args.extents)

    mapquest_geo= getgeo_mapquest(ip_list)
    generate_map("mapquest-"+output, mapquest_geo, wesn=args.extents)

if __name__ == '__main__':
    main()
