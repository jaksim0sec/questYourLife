#!/usr/bin/env python3
"""Compile the supplied, canonical SVG icons to Android VectorDrawable resources.

Only explicit SVG primitives in this asset pack are supported. Unknown elements
fail rather than silently approximating artwork. SVGs remain byte-for-byte sources.
"""
from pathlib import Path
import xml.etree.ElementTree as ET
import re

ROOT = Path(__file__).resolve().parents[1]
SOURCE = ROOT / 'app/src/main/assets/icons/svg'
TARGET = ROOT / 'app/src/main/res/drawable'
ANDROID = 'http://schemas.android.com/apk/res/android'
ET.register_namespace('android', ANDROID)


def geometry(element):
    name = element.tag.rsplit('}', 1)[-1]
    a = element.attrib
    if name == 'path':
        return a['d']
    if name == 'line':
        return f"M{a['x1']},{a['y1']} L{a['x2']},{a['y2']}"
    if name == 'circle':
        x, y, r = (float(a[k]) for k in ('cx', 'cy', 'r'))
        return f'M{x-r:g},{y:g} A{r:g},{r:g} 0,1 0 {x+r:g},{y:g} A{r:g},{r:g} 0,1 0 {x-r:g},{y:g} Z'
    if name in ('polygon', 'polyline'):
        parts = a['points'].replace(',', ' ').split()
        assert len(parts) % 2 == 0
        pairs = [','.join(parts[i:i+2]) for i in range(0, len(parts), 2)]
        return 'M' + ' L'.join(pairs) + (' Z' if name == 'polygon' else '')
    raise ValueError(f'Unsupported SVG primitive: {name}')


def color(value):
    if value == 'none':
        return '#00000000'
    if value == 'currentColor':
        return '#111111'  # Android VectorDrawable requires an explicit color.
    if len(value) == 4 and value.startswith('#'):
        return '#' + ''.join(c * 2 for c in value[1:])
    return value


def compile_icon(source):
    svg = ET.parse(source).getroot()
    assert svg.attrib['viewBox'] == '0 0 24 24'
    attrs = {'width': '24dp', 'height': '24dp', 'viewportWidth': '24', 'viewportHeight': '24'}
    vector = ET.Element('vector', {f'{{{ANDROID}}}{k}': v for k, v in attrs.items()})
    def emit(parent, element, inherited):
        inherited = inherited | element.attrib
        if element.tag.rsplit('}', 1)[-1] == 'g':
            transform = element.attrib.get('transform', '')
            match = re.fullmatch(r'translate\(([-\d.]+) ([-\d.]+)\) scale\(([-\d.]+)\) translate\(([-\d.]+) ([-\d.]+)\)', transform)
            if not match:
                raise ValueError(f'Unsupported SVG group transform: {transform}')
            x, y, scale, nx, ny = map(float, match.groups())
            assert x == -nx and y == -ny, 'Only centered scaling is supported'
            group = ET.SubElement(parent, 'group', {f'{{{ANDROID}}}{k}': f'{v:g}' for k, v in
                {'pivotX': x, 'pivotY': y, 'scaleX': scale, 'scaleY': scale}.items()})
            for child in element:
                emit(group, child, inherited)
            return
        assert 'transform' not in element.attrib, 'Primitive transforms must be represented by a group'
        path = {'pathData': geometry(element), 'fillColor': color(inherited.get('fill', '#000')),
                'strokeColor': color(inherited.get('stroke', 'none')),
                'strokeWidth': inherited.get('stroke-width', '1'),
                'strokeLineCap': inherited.get('stroke-linecap', 'butt'),
                'strokeLineJoin': inherited.get('stroke-linejoin', 'miter')}
        ET.SubElement(parent, 'path', {f'{{{ANDROID}}}{k}': v for k, v in path.items()})
    for element in svg:
        emit(vector, element, svg.attrib)
    ET.indent(vector, space='    ')
    output = TARGET / ('ic_patch_' + source.stem.replace('-', '_') + '.xml')
    ET.ElementTree(vector).write(output, encoding='utf-8', xml_declaration=True)
    return output


if __name__ == '__main__':
    icons = sorted(SOURCE.glob('*.svg'))
    assert len(icons) >= 12, 'Missing original 12-icon source pack'
    for icon in icons:
        print(compile_icon(icon).relative_to(ROOT))
