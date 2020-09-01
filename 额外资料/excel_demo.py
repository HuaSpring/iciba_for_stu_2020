import pandas as pd
import numpy as np
import os
from os import system

columns = ['name', 'id', 'sch', 'class']
df1 = pd.read_excel('C:/Users/admin/Desktop/1.xlsx', names=columns)
df2 = pd.read_excel('C:/Users/admin/Desktop/4.xlsx', names=columns)
file_name = 'idsNames.txt'

if not os.path.exists(file_name):
    os.system(r'touch %s' %file_name)

fd = open('./idsNames.txt',mode='w',encoding='utf-8')

def getidAndName(df):
    l = df.values.tolist()
    print(l)
    ret = []
    for i in l:
        v = str(i[1]) + " : " + i[0]
        print(v)
        fd.write(v)
        fd.write('\r')
        ret.append(v)
    return ret


getidAndName(df2)
getidAndName(df1)
