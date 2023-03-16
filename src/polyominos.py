def str2polyomino(polystr, cellch = 'x', minimizecoords = True):
    a = []
    row = 0
    col = 0
    lines = polystr.splitlines()
    # skip initial blank lines
    skip = 0
    while lines[skip] == '': skip += 1
    # scan lines for cellch
    for ln in lines[skip:]:
        col = 0
        for c in ln:
            if c == cellch:
                a.append((col,row))
            col += 1
        row += 1
    if minimizecoords:
        # shift coordinates so that the minimum x and y values are both zero
        col = lambda e: e[0]
        row = lambda e: e[1]
        minx = min(map(col, a))
        miny = min(map(row, a))
        if minx != 0 or miny != 0:
            a = [(x-minx, y-miny) for (x,y) in a]
    return a

domino = str2polyomino('xx', 'x')
v = '''
x
x
'''
vdomino = str2polyomino(v, 'x')
trominostr = '''
000
11.
1..
'''
tetrominostr = '''
00002
11122
133.2
33.44
...44
'''

trominos = [str2polyomino(trominostr, str(d)) for d in range(2)]
tetrominos = [str2polyomino(tetrominostr, str(d)) for d in range(5)]

def polydims(p):
    def minmax(a, fieldfunc):
        low = fieldfunc(a[0])
        high = low
        for e in a:
            val = fieldfunc(e)
            if val < low:
                low = val
            elif val > high:
                high = val
        return (low, high)
    
    col = lambda e: e[0]
    row = lambda e: e[1]
    
    colmin, colmax = minmax(p, col)
    rowmin, rowmax = minmax(p, row)
    return (colmax - colmin + 1, rowmax - rowmin + 1)

def polyshift(p, xoffset, yoffset):
    return [(x+xoffset, y+yoffset) for (x,y) in p]

def polyprint(p, gridwidth = None, gridheight = None, out = sys.stdout, polydims = polydims):
    cols, rows = polydims(p)
    if not gridheight:
        gridheight = rows
    if not gridwidth:
        gridwidth = cols
    for y in xrange(gridheight):
        for x in xrange(gridwidth):
            if (x,y) in p:
                out.write('x')
            else:
                out.write('.')
        out.write('\n')

def printallpositions(p, gridwidth, gridheight, polydims = polydims, polyprint = polyprint, polyshift = polyshift):
    cols, rows = polydims(p)
    for yoffset in range(gridheight-rows+1):
        for xoffset in range(gridwidth-cols+1):
            polyprint(polyshift(p, xoffset, yoffset), gridwidth, gridheight)
            print ' '

def polyprintmatrixrow(p, gridwidth, gridheight, precols = [], postcols = [], out = sys.stdout):
    if precols != []:
        prestr = ' '.join(map(str, precols)) + ' '
        out.write(prestr)
    for y in xrange(gridheight):
        for x in xrange(gridwidth):
            if (x,y) in p:
                out.write('1 ')
            else:
                out.write('0 ')
    if postcols != []:
        poststr = ' '.join(map(str, postcols))
        out.write(poststr)
    out.write('\n')

def printallmatrixrows(p, gridwidth, gridheight, precols = [], postcols = [], out = sys.stdout, polydims = polydims, polyshift = polyshift, polyprintmatrixrow = polyprintmatrixrow):
    cols, rows = polydims(p)
    for yoffset in range(gridheight-rows+1):
        for xoffset in range(gridwidth-cols+1):
            polyprintmatrixrow(polyshift(p, xoffset, yoffset), gridwidth, gridheight, precols, postcols, out)
