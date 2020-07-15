#
# Copyright (C) 2017 Black Duck Software, Inc.
# http://www.blackducksoftware.com/
#
#
# Licensed to the Apache Software Foundation (ASF) under one
# or more contributor license agreements. See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership. The ASF licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License. You may obtain a copy of the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing,
# software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
# KIND, either express or implied. See the License for the
# specific language governing permissions and limitations
# under the License.
#

import pkg_resources
import os
import sys
import getopt
import pip

pip_major_version = int(pip.__version__.split(".")[0])
if pip_major_version >= 20:
    from pip._internal.req import parse_requirements
    from pip._internal.network.session import PipSession
elif pip_major_version >= 10:
    from pip._internal.req import parse_requirements
    from pip._internal.download import PipSession
else:
    from pip.req import parse_requirements
    from pip.download import PipSession

def main():
    try:
        opts, args = getopt.getopt(sys.argv[1:], 'p:r', ['projectname=', 'requirements='])
    except getopt.GetoptError as error:
        print(str(error))
        print('integration-pip-inspector.py -projectname=<project_name> -requirements=<requirements_path>')
        sys.exit(2)

    project_name = None
    requirements_path = None

    for opt, arg in opts:
        if opt in '--projectname':
            project_name = arg
        elif opt in '--requirements':
            requirements_path = arg

    project = None

    if project_name is not None:
        project = resolve_package_by_name(project_name, [])

    if project is None:
        project = DependencyNode()
        project.name = 'n?'
        project.version = 'v?'

    if requirements_path is not None:
        try:
            assert os.path.exists(requirements_path), ("The requirements file %s does not exist." % requirements_path)
            requirements = parse_requirements(requirements_path, session=PipSession())
            for req in requirements:
                try:
                    requirement = resolve_package_by_name(req.req.name, [])
                    if requirement is None:
                        raise Exception()
                    project.children = project.children + [requirement]
                except:
                    if req is not None and req.req is not None:
                        print('--' + req.req.name)
        except AssertionError:
            print('r?' + requirements_path)
        except:
            print('p?' + requirements_path)

    print(project.render())


class DependencyNode(object):
    name = None
    version = None
    children = []

    def render(self, layer=1):
        result = self.name + "==" + self.version
        for child in self.children:
            result += "\n" + (" " * 4 * layer)
            result += child.render(layer + 1)
        return result


def get_package_by_name(package_name):
    try:
        return pkg_resources.working_set.by_key[package_name]
    except:
        pass
    try:
        return pkg_resources.working_set.by_key[package_name.lower()]
    except:
        pass
    try:
        return pkg_resources.working_set.by_key[package_name.replace('-','_')]
    except:
        pass
    try:
        return pkg_resources.working_set.by_key[package_name.replace('_','-')]
    except:
        pass
    return None


# Returns a DependencyNode
def resolve_package_by_name(package_name, history):
    node = DependencyNode()
    package = get_package_by_name(package_name)
    if package is None:
        return None
    node.name = package.project_name
    node.version = package.version
    if package_name.lower() not in history:
        for req in package.requires():
            child_node = resolve_package_by_name(req.key, history + [package_name.lower()])
            if child_node is not None:
                node.children = node.children + [child_node]
    return node

main()
