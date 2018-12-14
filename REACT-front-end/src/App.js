import React, { Component } from 'react';
import axios from 'axios';
//import PropTypes from 'prop-types'
import './App.css';
import {DEFAULT_QUERY,DEFAULT_HPP, PATH_BASE, PATH_SEARCH, PARAM_SEARCH, PARAM_PAGE, PARAM_HPP,} from './constants/index.js';
import {Search,Table,Button} from './Components/index.js'

// App ES6 class component
const updateSearchTopStoriesState = (hits, page) => (prevState) => {
  const { searchKey, results } = prevState;

  const oldHits = results && results[searchKey]
    ? results[searchKey].hits
    : [];

  const updatedHits = [
    ...oldHits,
    ...hits
  ];

  return {
    results: {
      ...results,
      [searchKey]: { hits: updatedHits, page }
    },
    isLoading: false
  };
};
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
    };

    this.needsToSearchTopStories = this.needsToSearchTopStories.bind(this);
    this.setSearchTopStories = this.setSearchTopStories.bind(this);
    this.fetchSearchTopStories = this.fetchSearchTopStories.bind(this);
    this.onSearchChange = this.onSearchChange.bind(this);
    this.onSearchSubmit = this.onSearchSubmit.bind(this);
    this.onDismiss = this.onDismiss.bind(this);
    //this.onSort = this.onSort.bind(this);
  }

    needsToSearchTopStories(searchTerm) {
    return !this.state.results[searchTerm];
  }

  setSearchTopStories(result) {
    // pull the hit and page from the result
    const { hits, page } = result;
    this.setState(updateSearchTopStoriesState(hits, page));
  }

fetchSearchTopStories(searchTerm, page = 0) {
  this.setState({ isLoading: true });
  //fetch(`${PATH_BASE}${PATH_SEARCH}?${PARAM_SEARCH}${searchTerm}&${PARAM_PAGE}${page}&${PARAM_HPP}${DEFAULT_HPP}`)
  
    //.then(response => response.json())
    //.then(result => this.setSearchTopStories(result))
    //.then(result => this._isMounted && this.setSearchTopStories(result.data)) //modified to handle React unmounted component
    // VV UNCOMMENT FOR ASYNC
    // axios(`${PATH_BASE}${PATH_SEARCH}?${PARAM_SEARCH}${searchTerm}&${PARAM_PAGE}${page}&${PARAM_HPP}${DEFAULT_HPP}`)
   // .then(result => this.setSearchTopStories(result.data))
 //   .catch(error => this._isMounted && this.setState({ error }));

}


  // component life cycle method
  componentDidMount() {
    this._isMounted = true;

    const { searchTerm } = this.state;
    this.setState({ searchKey: searchTerm });
    this.fetchSearchTopStories(searchTerm);
  }

  componentWillUnmount() {
    this._isMounted = false;
  }

  // Pass in event value to update local state variable
  onSearchChange(event) {
    this.setState({ searchTerm: event.target.value });
    //this.fetchSearchTopStories(event.target.value);
    //event.preventDefault();
  }

  onSearchSubmit(event) {
    const { searchTerm } = this.state;
    this.setState({ searchKey: searchTerm });

    if (this.needsToSearchTopStories(searchTerm)) {
      this.fetchSearchTopStories(searchTerm);
    }

    event.preventDefault();
  }

  onDismiss(id) {
    const { searchKey, results } = this.state;
    const { hits, page } = results[searchKey];

    const isNotId = item => item.objectID !== id;
    //const updateHits = this.state.result.hits.filter(isNotId);
    const updatedHits = hits.filter(isNotId);

    this.setState({
      //result: Object.assign({}, this.state.result,{hits:updateHits})
      results: {
        ...results,
        [searchKey]: { hits: updatedHits, page }
      }
    });
    //const updateList = this.state.list.filter(item => item.objectID !== id);
    //this.setState({ list: updateList })
  }
  //define new class method in componenet

  onSort(sortKey) {
    const isSortReverse = this.state.sortKey === sortKey && !this.state.isSortReverse;
    this.setState({ sortKey, isSortReverse });
  }
  
  render() {
    //const {searchTerm,list} = this.state;
    const { 
      searchTerm, 
      results, 
      searchKey, 
      error,
      isLoading
    } = this.state;


    // const page = (
    //   results && 
    //   results[searchKey] && 
    //   results[searchKey].page
    //   ) || 0;

    const page = 0;
    // const list = (
    //   results && 
    //   results[searchKey] && 
    //   results[searchKey].hits
    //   ) || [];

    const list = [
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

    return (
      <div className="page">
      <h1>Camel Crawler</h1>
      <h3>Where camels scour the web, not spiders</h3>
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
            list={list}            
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