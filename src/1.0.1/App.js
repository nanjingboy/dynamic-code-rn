import React, { Component } from 'react';
import { StyleSheet, Text, View, Image } from 'react-native';
import logo from './logo.png';

export default class App extends Component {
  render() {
    return (
      <View style={styles.container}>
        <Image source={logo} />
        <Text style={styles.text}>Hello V1.0.1!</Text>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  text: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
});
