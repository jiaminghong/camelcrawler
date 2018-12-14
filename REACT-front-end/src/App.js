import React, { Component } from 'react';
import axios from 'axios';

import './App.css';
import { DEFAULT_QUERY, DEFAULT_HPP, PATH_BASE, PATH_SEARCH, PARAM_SEARCH, PARAM_PAGE, PARAM_HPP, } from './constants/index.js';
import { Search, Table, Button } from './Components/index.js'
import { SSL_OP_DONT_INSERT_EMPTY_FRAGMENTS } from 'constants';

// App ES6 class component
// const updateSearchTopStoriesState = (hits, page) => (prevState) => {
//   const { searchKey, results } = prevState;

//   const oldHits = results && results[searchKey]
//     ? results[searchKey].hits
//     : [];

//   const updatedHits = [
//     ...oldHits,
//     ...hits
//   ];

//   return {
//     results: {
//       ...results,
//       [searchKey]: { hits: updatedHits, page }
//     },
//     isLoading: false
//   };
// };


class App extends Component {
  _isMounted = false;

  constructor(props) {
    super(props);

    this.state = {
      results: null,
      searchKey: '',
      searchTerm: DEFAULT_QUERY,
      error: null,
      isLoading: false,
      sortKey: 'NONE',
      isSortReverse: false,
      urls:null,
      tempList:[],
      data:null,
      links:[]   
    };

    this.needsToSearchTopStories = this.needsToSearchTopStories.bind(this);
    this.setSearchTopStories = this.setSearchTopStories.bind(this);
    this.fetchSearchTopStories = this.fetchSearchTopStories.bind(this);
    this.onSearchChange = this.onSearchChange.bind(this);
    this.onSearchSubmit = this.onSearchSubmit.bind(this);
    this.onDismiss = this.onDismiss.bind(this);
    this.convertJson = this.convertJson.bind(this);

  }

  needsToSearchTopStories(searchTerm) {
    return !this.state.results[searchTerm];
  }

  setSearchTopStories(result) {
    
    this.state.isLoading = false
    this.state.tempList = [{link: "ming.fun.com"}]
    console.log("New Results")
    console.log(this.state.results)
    //this.setState(updateSearchTopStoriesState(result));
  }
  

  // fetchSearchTopStories(searchTerm, page = 0) {
  //   this.setState({ isLoading: true });
  //   axios(`${PATH_BASE}${PATH_SEARCH}?${PARAM_SEARCH}${searchTerm}&${PARAM_PAGE}${page}&${PARAM_HPP}${DEFAULT_HPP}`)
  //     .then(result => this.setSearchTopStories(result.data))
  //     .catch(error => this._isMounted && this.setState({ error }));
  // }
  
  fetchSearchTopStories(searchTerm, page = 0) {
    this.setState({ isLoading: true });
    axios(`http://localhost:9000/data/https://www.firesticktricks.com/dmca-policy`)
      .then(result => this.setSearchTopStories(result))
      .catch(error => this._isMounted && this.setState({ error }));
  }
  // component life cycle method
  componentDidMount() {
    this._isMounted = true;

    const { searchTerm } = this.state;
    this.setState({ searchKey: searchTerm });
    this.fetchSearchTopStories(searchTerm);


    fetch(`http://localhost:9000/data/https://www.firesticktricks.com/dmca-policy`)
    // fetch(`${PATH_BASE}${PATH_SEARCH}?${PARAM_SEARCH}${searchTerm}&${PARAM_PAGE}${0}&${PARAM_HPP}${DEFAULT_HPP}`)
    .then(response => response.json())
    .then(data => this.setState({links:data}))
  }

  componentWillUnmount() {
    this._isMounted = false;
  }

  onSearchChange(event) {
    this.setState({ searchTerm: event.target.value });
  }
  
  onSearchSubmit(event){
    const { searchTerm } = this.state;
    this.setState({ searchKey: searchTerm });
    console.log("calling API at: http://localhost:9000/data/" + searchTerm)
    fetch(`http://localhost:9000/data/${searchTerm}`)
    // fetch(`${PATH_BASE}${PATH_SEARCH}?${PARAM_SEARCH}${searchTerm}&${PARAM_PAGE}${0}&${PARAM_HPP}${DEFAULT_HPP}`)
    .then(response => response.json())
    .then(data => this.setState({links:data}))

    event.preventDefault();
  }
  



  onDismiss(id) {
    const { searchKey, results } = this.state;
    // const { hits, page } = results[searchKey];
    // const isNotId = item => item.objectID !== id;
    // const updatedHits = hits.filter(isNotId);
    console.log("calling API at: http://localhost:9000/datawizspam/" + this.state.searchKey)
    fetch(`http://localhost:9000/datawizspam/${this.state.searchKey}`)
    // fetch(`${PATH_BASE}${PATH_SEARCH}?${PARAM_SEARCH}${searchTerm}&${PARAM_PAGE}${0}&${PARAM_HPP}${DEFAULT_HPP}`)
    .then(response => response.json())
    .then(data => alert(data.spam))

    // this.setState({
    //   results: {
    //     ...results,
    //     [searchKey]: { hits: updatedHits, page }
    //   }
    // });
  }




  onSort(sortKey) {
    const isSortReverse = this.state.sortKey === sortKey && !this.state.isSortReverse;
    this.setState({ sortKey, isSortReverse });
  }

  convertJson(listo){
    console.log(this.state.results)
  }

  render() {

    const {
      searchTerm,
      results,
      searchKey,
      error,
      isLoading,
      listo,
      links,
    } = this.state;

    const page = 0;
    const urls = [
      {
        title: 'React',
      },
      {
        title: 'Redux',
      },
    ];
    

    const tempList = [{link: "https://www.firesticktricks.com/ipvanish-review.html"},{link: "https://www.firesticktricks.com/how-to-use-kodi.html"},{link: "https://www.firesticktricks.com/install-sportsdevil-on-kodi.html"},{link: "https://www.firesticktricks.com/misfit-mods-lite-kodi-build.html"}]


    const lists = [
      {
        title: 'React',
        url: 'https://reactjs.org/',
        author: 'Jordan Walke',
        num_comments: 3,
        points: 4,
        objectID: 0,
      },
      {
        title: 'Redux',
        url: 'https://redux.js.org/',
        author: 'Dan Abramov, Andrew Clark',
        num_comments: 2,
        points: 5,
        objectID: 1,
      },
    ];

    const list = (
      lists
    ) || [];
    

    return (
      
      <div className="page">
        <h1>Camel Crawler</h1>
        <h3>Where camels scour the web, not spiders</h3>
        <div className="interactions">
        </div>
        <hr></hr>
        <div className="interactions">
          <Search
            value={searchTerm}
            onChange={this.onSearchChange}
            onSubmit={this.onSearchSubmit}
          >
            Search
      </Search>
        </div>
        {error
          ? <div className="interactions">
            <p>Something went wrong.</p>
          </div>
          : <Table
            list={links}
            onDismiss={this.onDismiss}
          />
        }
        <div className="interactions">
          <ButtonWithLoading
            isLoading={isLoading}
            onClick={() => this.fetchSearchTopStories(searchKey, page + 1)}>
            More
        </ButtonWithLoading>
        </div>
      </div>
    );
  }
}

const Loading = () =>
  <div>Loading ...</div>

const withLoading = (Component) => ({ isLoading, ...rest }) =>
  isLoading
    ? <Loading />
    : <Component {...rest} />

const ButtonWithLoading = withLoading(Button);

export {
  Button,
  Search,
  Table,
};



export default App;